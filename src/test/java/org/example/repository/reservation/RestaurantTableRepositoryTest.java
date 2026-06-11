package org.example.repository.reservation;

import org.example.model.reservation.Reservation;
import org.example.model.reservation.ReservationStatus;
import org.example.model.reservation.RestaurantTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class RestaurantTableRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RestaurantTableRepository restaurantTableRepository;

    @Test
    public void findAllFreeTest(){
        LocalDateTime now = LocalDateTime.now();
        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 5);
        RestaurantTable restaurantTable2 = new RestaurantTable("Stolik 2", 3);
        RestaurantTable restaurantTable3 = new RestaurantTable("Stolik 3", 7);
        restaurantTable3.setActive(false);


        entityManager.persist(restaurantTable);
        entityManager.persist(restaurantTable2);
        entityManager.persist(restaurantTable3);

        Reservation reservation = new Reservation("test@wp.pl", restaurantTable, now, now.plusHours(2), 50, "00000", ReservationStatus.CONFIRMED);
        Reservation reservation2 = new Reservation("test@wp.pl", restaurantTable2, now, now.plusHours(2), 50, "00000", ReservationStatus.CANCELLED);

        entityManager.persist(reservation);
        entityManager.persist(reservation2);

        List<RestaurantTable> restaurantTableList = restaurantTableRepository.findAllFreeTables(2, now, now.plusHours(2));
        Assertions.assertEquals(1, restaurantTableList.size());
        Assertions.assertEquals(restaurantTable2, restaurantTableList.get(0));

    }
}
