package org.example.model.reservation;

import org.example.model.user.Client;
import org.example.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;


public class ReservationTest {

    @Test
    public void getTest() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusHours(2);
        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 5);
        Reservation reservation = new Reservation("user@wp.pl", restaurantTable, startTime, endTime, 120, "123456", ReservationStatus.CONFIRMED);

        Assertions.assertEquals("user@wp.pl", reservation.getEmail());
        Assertions.assertEquals(restaurantTable, reservation.getRestaurantTable());
        Assertions.assertEquals(startTime, reservation.getStartTime());
        Assertions.assertEquals(endTime, reservation.getEndTime());
        Assertions.assertEquals(120, reservation.getPrice());
        Assertions.assertEquals("123456", reservation.getReservationCode());
        Assertions.assertEquals(ReservationStatus.CONFIRMED, reservation.getReservationStatus());
    }

    @Test
    public void setTest() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusHours(2);
        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 5);
        User client = new Client("user@wp.pl", "password", "Jan", "Nowak");

        Reservation reservation = new Reservation();
        reservation.setUser(client);
        reservation.setEmail("user@wp.pl");
        reservation.setRestaurantTable(restaurantTable);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setPrice(120);
        reservation.setReservationCode("123456");
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);

        Assertions.assertEquals(client, reservation.getUser());
        Assertions.assertEquals("user@wp.pl", reservation.getUser().getEmail());
        Assertions.assertEquals(restaurantTable, reservation.getRestaurantTable());
        Assertions.assertEquals(startTime, reservation.getStartTime());
        Assertions.assertEquals(endTime, reservation.getEndTime());
        Assertions.assertEquals(120, reservation.getPrice());
        Assertions.assertEquals("123456", reservation.getReservationCode());
        Assertions.assertEquals(ReservationStatus.CONFIRMED, reservation.getReservationStatus());
    }
}
