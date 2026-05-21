package org.example.service.reservation;

import org.example.dto.reservation.ReservationMapper;
import org.example.dto.reservation.ReservationRequestDto;
import org.example.dto.reservation.ReservationResponseDto;
import org.example.exception.ReservationTimeConflictException;
import org.example.exception.RestaurantTableNotFoundException;
import org.example.exception.TablePriceNotFoundException;
import org.example.model.reservation.Reservation;
import org.example.model.reservation.ReservationStatus;
import org.example.model.reservation.RestaurantTable;
import org.example.model.reservation.TablePrice;
import org.example.model.user.User;
import org.example.repository.reservation.ReservationRepository;
import org.example.repository.reservation.RestaurantTableRepository;
import org.example.repository.reservation.TablePriceRepository;
import org.example.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final RestaurantTableRepository restaurantTableRepository;
    private final TablePriceRepository tablePriceRepository;
    private final UserRepository userRepository;
    private static final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, ReservationMapper reservationMapper, RestaurantTableRepository restaurantTableRepository, TablePriceRepository tablePriceRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.restaurantTableRepository = restaurantTableRepository;
        this.tablePriceRepository = tablePriceRepository;
        this.userRepository = userRepository;
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
    public Reservation createReservation(ReservationRequestDto reservationRequestDto) {
        RestaurantTable restaurantTable = restaurantTableRepository.findWithLockById(reservationRequestDto.getTableId()).orElseThrow(RestaurantTableNotFoundException::new);
        TablePrice tablePrice = tablePriceRepository.findByNumberOfChairs(restaurantTable.getNumberOfChairs()).orElseThrow(TablePriceNotFoundException::new);
        User client = userRepository.findByEmail(reservationRequestDto.getEmail()).orElse(null);

        if(!reservationRepository.findByRestaurantTableAndStartTimeBeforeAndEndTimeAfter(restaurantTable, reservationRequestDto.getEndTime(), reservationRequestDto.getStartTime()).isEmpty()) {
            throw new ReservationTimeConflictException();
        }

        Reservation reservation = reservationMapper.toReservation(reservationRequestDto, client, restaurantTable);
        reservation.setPrice(tablePrice.getPrice());
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);
        reservation.setReservationCode(generateReservationCode());
        return reservationRepository.save(reservation);

    }

    private String generateReservationCode(){
        return String.format("%06d", secureRandom.nextInt(1000000));
    }
}
