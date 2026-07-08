package org.example.service.reservation;

import org.example.dto.balance.BalanceOperationDTO;
import org.example.dto.balance.BalanceOperationMapper;
import org.example.dto.reservation.*;
import org.example.dto.restaurantTable.FindFreeTablesRequest;
import org.example.dto.restaurantTable.FindFreeTablesResponse;
import org.example.exception.*;
import org.example.model.balance.BalanceOperation;
import org.example.model.balance.BalanceOperationType;
import org.example.model.reservation.*;
import org.example.model.restaurantTable.RestaurantTable;
import org.example.model.restaurantTable.RestaurantTableStatus;
import org.example.model.restaurantTable.TablePrice;
import org.example.model.user.Client;
import org.example.model.user.User;
import org.example.repository.balance.BalanceOperationRepository;
import org.example.repository.reservation.ReservationRepository;
import org.example.repository.restaurantTable.RestaurantTableRepository;
import org.example.repository.restaurantTable.TablePriceRepository;
import org.example.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final BalanceOperationMapper balanceOperationMapper;
    private static final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, ReservationMapper reservationMapper, RestaurantTableRepository restaurantTableRepository, TablePriceRepository tablePriceRepository, UserRepository userRepository, BalanceOperationRepository balanceOperationRepository, BalanceOperationMapper balanceOperationMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.restaurantTableRepository = restaurantTableRepository;
        this.tablePriceRepository = tablePriceRepository;
        this.userRepository = userRepository;
        this.balanceOperationRepository = balanceOperationRepository;
        this.balanceOperationMapper = balanceOperationMapper;
    }

    public List<ReservationResponseDto> getAllReservationsByEmail(String email) {
        List<Reservation> list =  reservationRepository.findAllByEmailAndReservationStatus(email, ReservationStatus.CONFIRMED);
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

        if(!reservationRepository.findByRestaurantTableAndStartTimeBeforeAndEndTimeAfterAndReservationStatusNot(restaurantTable, reservationRequestDto.getEndTime(), reservationRequestDto.getStartTime(), ReservationStatus.CANCELLED).isEmpty()) {
            throw new ReservationTimeConflictException();
        }

        int reservationLength = reservationRequestDto.getEndTime().getHour() - reservationRequestDto.getStartTime().getHour();
        double finalPrice = tablePrice.getPrice() * ((double) reservationLength / 2 );

        if(client instanceof Client) {
            if(((Client)client).getBalance() < finalPrice) {
                throw new InsufficientFundsException();
            }
            else {
                BalanceOperation balanceOperation = new BalanceOperation(client, LocalDateTime.now(), -finalPrice, ((Client)client).getBalance(), ((Client)client).getBalance() - finalPrice, BalanceOperationType.RESERVATION);
                ((Client)client).setBalance(((Client)client).getBalance() - finalPrice);
                userRepository.save(client);
                balanceOperationRepository.save(balanceOperation);
            }
        }

        Reservation reservation = reservationMapper.toReservation(reservationRequestDto, client, restaurantTable);
        reservation.setPrice(finalPrice);
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);
        reservation.setReservationCode(generateReservationCode());
        return reservationMapper.toReservationResponseDto(reservationRepository.save(reservation));
    }

    private String generateReservationCode(){
        return String.format("%06d", secureRandom.nextInt(1000000));
    }

    @Transactional
    public BalanceOperationDTO cancelReservationClient(long id, String email) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(ReservationNotFoundException::new);
        if(!reservation.getEmail().equals(email)) {
            throw new AccessDeniedException();
        }
        if(reservation.getReservationStatus() != ReservationStatus.CONFIRMED) {
            throw new ReservationStatusWrongTypeException();
        }
        if (reservation.getStartTime().isBefore(LocalDateTime.now())){
            throw new ReservationExpiredException();
        }

        reservation.setReservationStatus(ReservationStatus.CANCELLED);
        LocalDateTime now = LocalDateTime.now();
        long hoursUntil = Duration.between(LocalDateTime.now(), reservation.getStartTime()).toHours();

        double moneyBack;

        if(hoursUntil > 24){
            moneyBack = reservation.getPrice();
        }
        else if(hoursUntil > 12){
            moneyBack = reservation.getPrice() * 0.75;
        }
        else if(hoursUntil > 6){
            moneyBack = reservation.getPrice() * 0.5 ;
        }
        else {
            moneyBack = reservation.getPrice() * 0.2;
        }

        User client = reservation.getUser();
        BalanceOperation balanceOperation = new BalanceOperation(client, now, moneyBack, ((Client)client).getBalance(),  ((Client)client).getBalance() + moneyBack, BalanceOperationType.RESERVATION_CANCELLED);
        ((Client)client).setBalance(((Client)client).getBalance() + moneyBack);
        userRepository.save(client);
        reservationRepository.save(reservation);
        return balanceOperationMapper.fromBalanceOperation(balanceOperationRepository.save(balanceOperation));
    }

    public FindFreeTablesResponse findAllFreeRestaurantTables(FindFreeTablesRequest request) {
        LocalDateTime start = LocalDateTime.of(request.getReservationDay(), request.getReservationStartTime());
        LocalDateTime end = start.plusHours(request.getReservationLength());
        List<RestaurantTable> exact = restaurantTableRepository.findAllFreeTables(request.getMinNumberOfChairs(), start, end);
        List<RestaurantTable> earlier = restaurantTableRepository.findAllFreeTables(request.getMinNumberOfChairs(), start.minusHours(2), end.minusHours(2));
        List<RestaurantTable> later = restaurantTableRepository.findAllFreeTables(request.getMinNumberOfChairs(), start.plusHours(2), end.plusHours(2));

        FindFreeTablesResponse findFreeTablesResponse = new FindFreeTablesResponse();
        findFreeTablesResponse.setStartTime(start);
        findFreeTablesResponse.setEndTime(end);
        findFreeTablesResponse.setExactTables(exact);
        findFreeTablesResponse.setEarlierTables(earlier);
        findFreeTablesResponse.setLaterTables(later);

        return findFreeTablesResponse;
    }

    public List<NextReservationDTO> getNextReservations() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDay = now.with(LocalTime.MAX);
        LocalDateTime startDay = now.with(LocalTime.MIN);

        List<ReservationStatus> statuses = List.of(
                ReservationStatus.CONFIRMED,
                ReservationStatus.IN_PROGRESS
        );

        List<Reservation> reservationList = reservationRepository.findNextReservations(startDay, endDay, statuses);
        List<NextReservationDTO> nextReservations = new ArrayList<>();
        for(Reservation reservation : reservationList) {
            NextReservationDTO nextReservationDTO = reservationMapper.toNextReservationDTO(reservation);
            nextReservations.add(nextReservationDTO);
        }
        return nextReservations;
    }

    public List<ReservationWithTableDto> getTodaysReservationsByEmail(String email) {
        LocalDateTime endDay = LocalDateTime.now().with(LocalTime.MAX);
        LocalDateTime startDay = endDay.with(LocalTime.MIN);
        List<Reservation> reservationList = reservationRepository.findByEmailAndReservationStatusAndStartTimeAfterAndStartTimeBefore(email, ReservationStatus.CONFIRMED,startDay, endDay);

        List<ReservationWithTableDto> dtoList = new ArrayList<>();
        for(Reservation reservation : reservationList) {
            ReservationWithTableDto dto = reservationMapper.toReservationWithTableDTO(reservation);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Transactional
    public ReservationWithTableDto startReservation(StartReservationRequest startReservationRequest) {
        Reservation reservation = reservationRepository.findById(startReservationRequest.getReservationId()).orElseThrow(ReservationNotFoundException::new);
        RestaurantTable restaurantTable = restaurantTableRepository.findById(startReservationRequest.getTableId()).orElseThrow(RestaurantTableNotFoundException::new);

        if(!startReservationRequest.getReservationCode().equals(reservation.getReservationCode())) {
            throw new InvalidReservationCodeException();
        }

        if(!startReservationRequest.getVersion().equals(restaurantTable.getVersion())) {
            throw new RestaurantTableStateConflict();
        }

        if(restaurantTable.getStatus().equals(RestaurantTableStatus.OCCUPIED)){
            throw new RestaurantTableStateConflict();
        }

        reservation.setReservationStatus(ReservationStatus.IN_PROGRESS);
        restaurantTable.setStatus(RestaurantTableStatus.OCCUPIED);

        try {
            reservationRepository.save(reservation);
            restaurantTableRepository.save(restaurantTable);
            restaurantTableRepository.flush();
            return reservationMapper.toReservationWithTableDTO(reservation);
        }
        catch(Exception e){
            throw new RestaurantTableStateConflict();
        }

    }

    public ReservationWithTableDto getReservationWithTableDtoById(long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(ReservationNotFoundException::new);
        return reservationMapper.toReservationWithTableDTO(reservation);

    }
}
