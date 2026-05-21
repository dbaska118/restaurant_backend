package org.example.service.reservation;

import jakarta.persistence.EntityManager;
import org.example.dto.reservation.ReservationRequestDto;
import org.example.dto.reservation.ReservationResponseDto;
import org.example.exception.ReservationTimeConflictException;
import org.example.exception.RestaurantTableNotFoundException;
import org.example.exception.TablePriceNotFoundException;
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

        Assertions.assertThrows(RestaurantTableNotFoundException.class, () -> {
            reservationService.createReservation(dto);
        });

        entityManager.persist(restaurantTable);
        dto.setTableId(restaurantTable.getId());
        Assertions.assertThrows(TablePriceNotFoundException.class, () -> {
            reservationService.createReservation(dto);
        });


        entityManager.persist(tablePrice);
        Reservation reservation = reservationService.createReservation(dto);
        Assertions.assertNotNull(reservation);

        dto.setEmail("user2@wp.pl");
        Assertions.assertThrows(ReservationTimeConflictException.class, () -> {
            reservationService.createReservation(dto);
        });
    }
}
