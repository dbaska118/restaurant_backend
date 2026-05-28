package org.example.controller.reservation;

import jakarta.persistence.EntityManager;
import org.example.dto.reservation.BalanceOperationDTO;
import org.example.dto.reservation.ReservationRequestDto;
import org.example.dto.reservation.ReservationResponseDto;
import org.example.exception.InsufficientFundsException;
import org.example.exception.ReservationTimeConflictException;
import org.example.exception.RestaurantTableNotFoundException;
import org.example.exception.TablePriceNotFoundException;
import org.example.model.reservation.Reservation;
import org.example.model.reservation.ReservationStatus;
import org.example.model.reservation.RestaurantTable;
import org.example.model.reservation.TablePrice;
import org.example.model.user.Client;
import org.example.model.user.User;
import org.example.repository.reservation.ReservationRepository;
import org.example.service.reservation.ReservationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ReservationControllerTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ReservationController reservationController;

    @Test
    public void getReservationsByEmailTest(){
        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "client@wp.pl";
            }
        };

        ResponseEntity<List<ReservationResponseDto>> response = reservationController.getAllReservationsByEmail("test@wp.pl", principal);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        response = reservationController.getAllReservationsByEmail("client@wp.pl", principal);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void createReservationClient(){
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

        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "client@wp.pl";
            }
        };
        ResponseEntity<ReservationResponseDto> response = reservationController.createReservationClient(dto, principal);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());


        principal = new Principal() {
            @Override
            public String getName() {
                return "user@wp.pl";
            }
        };
        response = reservationController.createReservationClient(dto, principal);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        entityManager.persist(restaurantTable);
        dto.setTableId(restaurantTable.getId());
        response = reservationController.createReservationClient(dto, principal);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());


        entityManager.persist(tablePrice);
        response = reservationController.createReservationClient(dto, principal);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        client.setBalance(500);
        entityManager.persist(client);
        response = reservationController.createReservationClient(dto, principal);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        dto.setEmail("user2@wp.pl");
        principal = new Principal() {
            @Override
            public String getName() {
                return "user2@wp.pl";
            }
        };
        response = reservationController.createReservationClient(dto, principal);
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void cancelReservationClientTest(){
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now().minusDays(1).plusHours(2);
        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 5);
        entityManager.persist(restaurantTable);
        User client = new Client("user@wp.pl", "oassword", "Jan", "Nowak");
        entityManager.persist(client);

        Reservation reservation = new Reservation("user@wp.pl", restaurantTable, startTime, endTime, 100, "123456", ReservationStatus.CANCELLED);
        reservation.setUser(client);
        entityManager.persist(reservation);
        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "client@wp.pl";
            }
        };

        ResponseEntity<BalanceOperationDTO> response = reservationController.cancelReservationClient(reservation.getId(), principal);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        principal = new Principal() {
            @Override
            public String getName() {
                return "user@wp.pl";
            }
        };
        response = reservationController.cancelReservationClient(reservation.getId(), principal);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());


        reservation.setReservationStatus(ReservationStatus.CONFIRMED);
        entityManager.persist(reservation);
        response = reservationController.cancelReservationClient(reservation.getId(), principal);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        reservation.setStartTime(startTime.plusDays(2));
        reservation.setEndTime(endTime.plusDays(2));
        entityManager.persist(reservation);
        response = reservationController.cancelReservationClient(reservation.getId(), principal);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        response = reservationController.cancelReservationClient(-1L, principal);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
