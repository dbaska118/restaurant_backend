package org.example.service.reservation;

import jakarta.persistence.EntityManager;
import org.example.dto.balance.BalanceOperationDTO;
import org.example.dto.reservation.*;
import org.example.dto.restaurantTable.FindFreeTablesRequest;
import org.example.dto.restaurantTable.FindFreeTablesResponse;
import org.example.exception.*;
import org.example.model.reservation.Reservation;
import org.example.model.reservation.ReservationStatus;
import org.example.model.restaurantTable.RestaurantTable;
import org.example.model.restaurantTable.RestaurantTableStatus;
import org.example.model.restaurantTable.TablePrice;
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

    @Test
    public void findAllFreeRestaurantTablesTest(){
        LocalDateTime now = LocalDateTime.now();
        FindFreeTablesRequest request = new FindFreeTablesRequest();
        request.setReservationLength(2);
        request.setReservationDay(now.toLocalDate());
        request.setReservationStartTime(now.toLocalTime());
        request.setMinNumberOfChairs(4);

        RestaurantTable table1 = new RestaurantTable("Stolik 1", 6);
        RestaurantTable table2 = new RestaurantTable("Stolik 2", 5);
        entityManager.persist(table1);
        entityManager.persist(table2);

        Reservation reservation = new Reservation("test@wp.pl", table1, now.plusHours(1), now.plusHours(3), 50, "00000", ReservationStatus.CONFIRMED);
        Reservation reservation2 = new Reservation("test@wp.pl",table2, now, now.plusHours(2), 50, "00000", ReservationStatus.CANCELLED);
        Reservation reservation3 = new Reservation("test@wp.pl", table2, now.minusHours(2), now, 50, "00000", ReservationStatus.CONFIRMED);
        entityManager.persist(reservation);
        entityManager.persist(reservation2);
        entityManager.persist(reservation3);

        FindFreeTablesResponse response = reservationService.findAllFreeRestaurantTables(request);
        Assertions.assertEquals(now, response.getStartTime());
        Assertions.assertEquals(now.plusHours(2), response.getEndTime());
        Assertions.assertEquals(1, response.getExactTables().size());
        Assertions.assertEquals(table2, response.getExactTables().get(0));
        Assertions.assertEquals(1, response.getEarlierTables().size());
        Assertions.assertEquals(table1, response.getEarlierTables().get(0));
        Assertions.assertEquals(1, response.getLaterTables().size());
        Assertions.assertEquals(table2, response.getLaterTables().get(0));

    }

    @Test
    public void getNextReservationTest(){
        LocalDateTime now = LocalDateTime.now();
        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 5);
        RestaurantTable restaurantTable2 = new RestaurantTable("Stolik 2", 3);
        entityManager.persist(restaurantTable);
        entityManager.persist(restaurantTable2);


        Reservation reservation = new Reservation("text@wp.pl", restaurantTable, now.plusHours(4), now.plusHours(6), 100, "000001", ReservationStatus.CONFIRMED);
        Reservation reservation2 = new Reservation("client@wp.pl", restaurantTable, now, now.plusHours(2), 100, "000002", ReservationStatus.CONFIRMED);
        Reservation reservation3 = new Reservation("client@wp.pl", restaurantTable2, now, now.plusHours(2), 100, "000003", ReservationStatus.CANCELLED);
        entityManager.persist(reservation);
        entityManager.persist(reservation2);
        entityManager.persist(reservation3);

        List<NextReservationDTO> dtoList = reservationService.getNextReservations();
        Assertions.assertEquals(1, dtoList.size());
        Assertions.assertEquals(reservation2.getId(), dtoList.get(0).getId());
        Assertions.assertEquals(restaurantTable.getId(), dtoList.get(0).getTableId());
        Assertions.assertEquals(reservation2.getStartTime(), dtoList.get(0).getStartTime());
        Assertions.assertEquals(reservation2.getEndTime(), dtoList.get(0).getEndTime());


    }

    @Test
    public void getTodaysReservationsByEmailTest(){
        LocalDateTime now = LocalDateTime.now();
        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 5);
        RestaurantTable restaurantTable2 = new RestaurantTable("Stolik 2", 3);
        entityManager.persist(restaurantTable);
        entityManager.persist(restaurantTable2);


        Reservation reservation = new Reservation("text@wp.pl", restaurantTable, now.plusHours(4), now.plusHours(6), 100, "000001", ReservationStatus.CONFIRMED);
        Reservation reservation2 = new Reservation("client@wp.pl", restaurantTable, now, now.plusHours(2), 100, "000002", ReservationStatus.CONFIRMED);
        Reservation reservation3 = new Reservation("client@wp.pl", restaurantTable2, now, now.plusHours(2), 100, "000003", ReservationStatus.CANCELLED);
        entityManager.persist(reservation);
        entityManager.persist(reservation2);
        entityManager.persist(reservation3);

        List<ReservationWithTableDto> dtoList = reservationService.getTodaysReservationsByEmail("client@wp.pl");
        Assertions.assertEquals(1, dtoList.size());
        Assertions.assertEquals(reservation2.getId(), dtoList.get(0).getId());
        Assertions.assertEquals(reservation2.getRestaurantTable().getId(), dtoList.get(0).getRestaurantTableReservationDTO().getId());
    }

    @Test
    public void StartReservationTest(){
        StartReservationRequest request = new StartReservationRequest();
        request.setReservationId(-1L);
        request.setTableId(-1L);
        request.setReservationCode("000000");
        request.setVersion(156);

        LocalDateTime now = LocalDateTime.now();
        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 5);
        Reservation reservation = new Reservation("client@wp.pl", restaurantTable, now, now.plusHours(2), 100, "000002", ReservationStatus.CONFIRMED);

        Assertions.assertThrows(ReservationNotFoundException.class, () -> {
            reservationService.startReservation(request);
        });

        entityManager.persist(reservation);
        request.setReservationId(reservation.getId());
        Assertions.assertThrows(RestaurantTableNotFoundException.class, () -> {
            reservationService.startReservation(request);
        });

        entityManager.persist(restaurantTable);
        request.setTableId(restaurantTable.getId());
        Assertions.assertThrows(InvalidReservationCodeException.class, () -> {
            reservationService.startReservation(request);
        });

        request.setReservationCode("000002");
        Assertions.assertThrows(RestaurantTableStateConflict.class, () -> {
            reservationService.startReservation(request);
        });

        request.setVersion(restaurantTable.getVersion());
        restaurantTable.setStatus(RestaurantTableStatus.OCCUPIED);
        entityManager.persist(restaurantTable);
        Assertions.assertThrows(RestaurantTableStateConflict.class, () -> {
            reservationService.startReservation(request);
        });

        restaurantTable.setStatus(RestaurantTableStatus.FREE);
        entityManager.persist(restaurantTable);
        ReservationWithTableDto dto = reservationService.startReservation(request);
        Assertions.assertEquals(ReservationStatus.IN_PROGRESS, dto.getReservationStatus());
        Assertions.assertEquals(RestaurantTableStatus.OCCUPIED, dto.getRestaurantTableReservationDTO().getStatus());

    }

    @Test
    public void getReservationWithTableDtoByIdTest(){
        LocalDateTime now = LocalDateTime.now();
        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 5);
        Reservation reservation = new Reservation("client@wp.pl", restaurantTable, now, now.plusHours(2), 100, "000002", ReservationStatus.CONFIRMED);

        Assertions.assertThrows(ReservationNotFoundException.class, () -> {
            reservationService.getReservationWithTableDtoById(-1L);
        });

        entityManager.persist(restaurantTable);
        entityManager.persist(reservation);

        ReservationWithTableDto dto = reservationService.getReservationWithTableDtoById(reservation.getId());
        Assertions.assertEquals(dto.getId(), reservation.getId());
        Assertions.assertEquals(dto.getRestaurantTableReservationDTO().getId(), restaurantTable.getId());

    }
}
