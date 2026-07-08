package org.example.repository.reservation;


import org.example.model.reservation.Reservation;
import org.example.model.reservation.ReservationStatus;
import org.example.model.restaurantTable.RestaurantTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ReservationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservationRepository reservationRepository;


    @Test
    public void findNextReservationsTest() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDay = now.with(LocalTime.MAX);

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

        List<ReservationStatus> status = List.of(ReservationStatus.CONFIRMED);

        List<Reservation> reservations = reservationRepository.findNextReservations(now.minusHours(1), endDay, status);
        Assertions.assertEquals(1, reservations.size());
        Assertions.assertEquals(reservation2, reservations.get(0));
    }

    @Test
    public void findByRestaurantTableIdAndReservationStatusTest(){
        LocalDateTime now = LocalDateTime.now();

        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 5);
        RestaurantTable restaurantTable2 = new RestaurantTable("Stolik 2", 3);
        entityManager.persist(restaurantTable);
        entityManager.persist(restaurantTable2);


        Reservation reservation = new Reservation("text@wp.pl", restaurantTable, now.plusHours(4), now.plusHours(6), 100, "000001", ReservationStatus.CONFIRMED);
        Reservation reservation2 = new Reservation("client@wp.pl", restaurantTable2, now, now.plusHours(2), 100, "000002", ReservationStatus.CONFIRMED);
        entityManager.persist(reservation);
        entityManager.persist(reservation2);

        Optional<Reservation> reservationDB = reservationRepository.findByRestaurantTableIdAndReservationStatus(restaurantTable2.getId(), ReservationStatus.CONFIRMED);
        Assertions.assertTrue(reservationDB.isPresent());
        Assertions.assertEquals(reservation2, reservationDB.get());
    }
}
