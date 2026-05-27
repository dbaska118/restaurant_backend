package org.example.service.reservation;

import jakarta.persistence.EntityManager;
import org.example.dto.reservation.BalanceOperationDTO;
import org.example.dto.reservation.ReservationRequestDto;
import org.example.dto.reservation.ReservationResponseDto;
import org.example.exception.*;
import org.example.model.reservation.Reservation;
import org.example.model.reservation.ReservationStatus;
import org.example.model.reservation.RestaurantTable;
import org.example.model.reservation.TablePrice;
import org.example.model.user.Client;
import org.example.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;

    @Autowired
    EntityManager entityManager;

    @Test
    public void getAllByEmailTest(){
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusHours(2);
        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 5);
        entityManager.persist(restaurantTable);

        Reservation reservation = new Reservation("user@wp.pl", restaurantTable, startTime, endTime, 120, "123456", ReservationStatus.CONFIRMED);
        Reservation reservation2 = new Reservation("user2@wp.pl", restaurantTable, startTime, endTime, 120, "123456", ReservationStatus.CONFIRMED);
        entityManager.persist(reservation);
        entityManager.persist(reservation2);

        List<ReservationResponseDto> list = reservationService.getAllReservationsByEmail("user2@wp.pl");
        Assertions.assertEquals(1, list.size());
        Assertions.assertTrue(list.get(0) != null);
    }

    @Test
    public void createReservationTest(){
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusHours(2);
        TablePrice tablePrice = new TablePrice(4, 200);
        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 4);
        ReservationRequestDto dto = new ReservationRequestDto();
        dto.setStartTime(startTime);
        dto.setEndTime(endTime);
        dto.setTableId(-1L);
        dto.setEmail("user@wp.pl");
        Client client = new Client("user@wp.pl", "oassword", "Jan", "Nowak");
        client.setBalance(180);
        entityManager.persist(client);

        Assertions.assertThrows(RestaurantTableNotFoundException.class, () -> {
            reservationService.createReservation(dto);
        });

        entityManager.persist(restaurantTable);
        dto.setTableId(restaurantTable.getId());
        Assertions.assertThrows(TablePriceNotFoundException.class, () -> {
            reservationService.createReservation(dto);
        });


        entityManager.persist(tablePrice);
        Assertions.assertThrows(InsufficientFundsException.class, () -> {
            reservationService.createReservation(dto);
        });

        client.setBalance(500);
        entityManager.persist(client);
        ReservationResponseDto reservation = reservationService.createReservation(dto);
        Assertions.assertNotNull(reservation);

        dto.setEmail("user2@wp.pl");
        Assertions.assertThrows(ReservationTimeConflictException.class, () -> {
            reservationService.createReservation(dto);
        });
    }

    @Test
    public void cancelReservationTest(){
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now().minusDays(1).plusHours(2);
        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 5);
        entityManager.persist(restaurantTable);
        User client = new Client("user@wp.pl", "oassword", "Jan", "Nowak");
        entityManager.persist(client);

        Reservation reservation = new Reservation("user@wp.pl", restaurantTable, startTime, endTime, 100, "123456", ReservationStatus.CONFIRMED);
        reservation.setUser(client);

        Assertions.assertThrows(ReservationNotFoundException.class, () -> {
            reservationService.cancelReservationClient(-1L, "user@wp.pl");
        });

        entityManager.persist(reservation);

        Assertions.assertThrows(ReservationExpiredException.class, () -> {
            reservationService.cancelReservationClient(reservation.getId(), "user@wp.pl");
        });

        reservation.setReservationStatus(ReservationStatus.CANCELLED);
        entityManager.persist(reservation);
        Assertions.assertThrows(AccessDeniedException.class, () -> {
            reservationService.cancelReservationClient(reservation.getId(), "user2222@wp.pl");
        });
        Assertions.assertThrows(ReservationStatusWrongTypeException.class, () -> {
            reservationService.cancelReservationClient(reservation.getId(), "user@wp.pl");
        });


        reservation.setStartTime(LocalDateTime.now().plusDays(1).plusHours(2));
        reservation.setEndTime(LocalDateTime.now().plusDays(1).plusHours(4));
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);
        BalanceOperationDTO balanceOperationDTO = reservationService.cancelReservationClient(reservation.getId(), "user@wp.pl");
        Assertions.assertEquals(100, balanceOperationDTO.getAmount());

        reservation.setStartTime(LocalDateTime.now().plusHours(14));
        reservation.setEndTime(LocalDateTime.now().plusHours(16));
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);
        balanceOperationDTO = reservationService.cancelReservationClient(reservation.getId(), "user@wp.pl");
        Assertions.assertEquals(75, balanceOperationDTO.getAmount());

        reservation.setStartTime(LocalDateTime.now().plusHours(8));
        reservation.setEndTime(LocalDateTime.now().plusHours(10));
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);
        balanceOperationDTO = reservationService.cancelReservationClient(reservation.getId(), "user@wp.pl");
        Assertions.assertEquals(50, balanceOperationDTO.getAmount());

        reservation.setStartTime(LocalDateTime.now().plusHours(3));
        reservation.setEndTime(LocalDateTime.now().plusHours(5));
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);
        balanceOperationDTO = reservationService.cancelReservationClient(reservation.getId(), "user@wp.pl");
        Assertions.assertEquals(20, balanceOperationDTO.getAmount());
    }
}
