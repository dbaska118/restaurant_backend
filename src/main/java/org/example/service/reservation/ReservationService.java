package org.example.service.reservation;

import org.example.dto.reservation.ReservationMapper;
import org.example.dto.reservation.ReservationRequestDto;
import org.example.dto.reservation.ReservationResponseDto;
import org.example.exception.InsufficientFundsException;
import org.example.exception.ReservationTimeConflictException;
import org.example.exception.RestaurantTableNotFoundException;
import org.example.exception.TablePriceNotFoundException;
import org.example.model.reservation.*;
import org.example.model.user.Client;
import org.example.model.user.User;
import org.example.repository.reservation.BalanceOperationRepository;
import org.example.repository.reservation.ReservationRepository;
import org.example.repository.reservation.RestaurantTableRepository;
import org.example.repository.reservation.TablePriceRepository;
import org.example.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final RestaurantTableRepository restaurantTableRepository;
    private final TablePriceRepository tablePriceRepository;
    private final UserRepository userRepository;
    private final BalanceOperationRepository balanceOperationRepository;
    private static final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, ReservationMapper reservationMapper, RestaurantTableRepository restaurantTableRepository, TablePriceRepository tablePriceRepository, UserRepository userRepository, BalanceOperationRepository balanceOperationRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.restaurantTableRepository = restaurantTableRepository;
        this.tablePriceRepository = tablePriceRepository;
        this.userRepository = userRepository;
        this.balanceOperationRepository = balanceOperationRepository;
    }

    public List<ReservationResponseDto> getAllReservationsByEmail(String email) {
        List<Reservation> list =  reservationRepository.findAllByEmail(email);
        List<ReservationResponseDto> response = new ArrayList<>();
        for (Reservation reservation : list) {
            response.add(reservationMapper.toReservationResponseDto(reservation));
        }
        return response;
    }

    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto reservationRequestDto) {
        RestaurantTable restaurantTable = restaurantTableRepository.findWithLockById(reservationRequestDto.getTableId()).orElseThrow(RestaurantTableNotFoundException::new);
        TablePrice tablePrice = tablePriceRepository.findByNumberOfChairs(restaurantTable.getNumberOfChairs()).orElseThrow(TablePriceNotFoundException::new);
        User client = userRepository.findByEmail(reservationRequestDto.getEmail()).orElse(null);

        if(!reservationRepository.findByRestaurantTableAndStartTimeBeforeAndEndTimeAfter(restaurantTable, reservationRequestDto.getEndTime(), reservationRequestDto.getStartTime()).isEmpty()) {
            throw new ReservationTimeConflictException();
        }

        if(client instanceof Client) {
            if(((Client)client).getBalance() < tablePrice.getPrice()) {
                throw new InsufficientFundsException();
            }
            else {
                BalanceOperation balanceOperation = new BalanceOperation(client, LocalDateTime.now(), tablePrice.getPrice(), ((Client)client).getBalance(), ((Client)client).getBalance() - tablePrice.getPrice(), BalanceOperationType.RESERVATION);
                ((Client)client).setBalance(((Client)client).getBalance() - tablePrice.getPrice());
                userRepository.save(client);
                balanceOperationRepository.save(balanceOperation);
            }
        }

        Reservation reservation = reservationMapper.toReservation(reservationRequestDto, client, restaurantTable);
        reservation.setPrice(tablePrice.getPrice());
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);
        reservation.setReservationCode(generateReservationCode());
        return reservationMapper.toReservationResponseDto(reservationRepository.save(reservation));
    }

    private String generateReservationCode(){
        return String.format("%06d", secureRandom.nextInt(1000000));
    }
}
