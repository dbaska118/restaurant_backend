package org.example.dto.reservation;

import org.example.model.reservation.Reservation;
import org.example.model.reservation.ReservationStatus;
import org.example.model.reservation.RestaurantTable;
import org.example.model.user.Client;
import org.example.model.user.User;
import org.example.service.dish.DishService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ReservationMapper.class)
@ActiveProfiles("test")
public class ReservationMapperTest {

    private final ReservationMapper mapper = new ReservationMapper();

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void toReservationPublicDTOTest(){
        User client = new Client("client@wp.pl", "password", "Jan", "Nowak");
        RestaurantTable table = new RestaurantTable("Stolik 1", 4);
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = new Reservation("client@wp.pl", table, now, now.plusHours(2), 250, "000123", ReservationStatus.CONFIRMED);
        reservation.setUser(client);
        entityManager.persist(client);
        entityManager.persist(table);
        entityManager.persist(reservation);


        ReservationPublicDto dto = mapper.toReservationPublicDTO(reservation);
        Assertions.assertEquals(table.getId(), dto.getTableId());
        Assertions.assertEquals(now, dto.getStartTime());
        Assertions.assertEquals(now.plusHours(2), dto.getEndTime());
    }

    @Test
    public void toReservationResponseDto(){
        User client = new Client("client@wp.pl", "password", "Jan", "Nowak");
        RestaurantTable table = new RestaurantTable("Stolik 1", 4);
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = new Reservation("client@wp.pl", table, now, now.plusHours(2), 250, "000123", ReservationStatus.CONFIRMED);
        reservation.setUser(client);

        entityManager.persist(client);
        entityManager.persist(table);
        entityManager.persist(reservation);

        ReservationResponseDto dto = mapper.toReservationResponseDto(reservation);
        Assertions.assertEquals(reservation.getId(), dto.getId());
        Assertions.assertEquals("client@wp.pl", dto.getEmail());
        Assertions.assertEquals("Stolik 1", dto.getTableName());
        Assertions.assertEquals(now, dto.getStartTime());
        Assertions.assertEquals(now.plusHours(2), dto.getEndTime());
        Assertions.assertEquals(250, dto.getPrice());
        Assertions.assertEquals("000123", dto.getReservationCode());
        Assertions.assertEquals(ReservationStatus.CONFIRMED, dto.getReservationStatus());
        Assertions.assertEquals(4, dto.getNumberOfChairs());
    }

    @Test
    public void toReservationTest(){
        User client = new Client("client@wp.pl", "password", "Jan", "Nowak");
        RestaurantTable table = new RestaurantTable("Stolik 1", 4);

        entityManager.persist(client);
        entityManager.persist(table);

        LocalDateTime now = LocalDateTime.now();
        ReservationRequestDto dto = new ReservationRequestDto();
        dto.setEmail("client@wp.pl");
        dto.setTableId(table.getId());
        dto.setStartTime(now);
        dto.setEndTime(now.plusHours(2));


        Reservation reservation = mapper.toReservation(dto, client, table);
        Assertions.assertEquals("client@wp.pl", reservation.getEmail());
        Assertions.assertEquals(client, reservation.getUser());
        Assertions.assertEquals(table, reservation.getRestaurantTable());
        Assertions.assertEquals(now, reservation.getStartTime());
        Assertions.assertEquals(now.plusHours(2), reservation.getEndTime());
    }
}
