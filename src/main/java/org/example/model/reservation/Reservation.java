package org.example.model.reservation;

import jakarta.persistence.*;
import org.example.model.user.*;

import java.time.LocalDateTime;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private String email;

    @ManyToOne
    private RestaurantTable restaurantTable;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private double price;

    private String reservationCode;

    private ReservationStatus reservationStatus;

    public Reservation() {
    }

    public Reservation(String email, RestaurantTable restaurantTable, LocalDateTime startTime, LocalDateTime endTime, double price, String reservationCode, ReservationStatus reservationStatus) {
        this.email = email;
        this.restaurantTable = restaurantTable;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.reservationCode = reservationCode;
        this.reservationStatus = reservationStatus;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getEmail() {
        return email;
    }

    public RestaurantTable getRestaurantTable() {
        return restaurantTable;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public double getPrice() {
        return price;
    }

    public String getReservationCode() {
        return reservationCode;
    }

    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setRestaurantTable(RestaurantTable restaurantTable) {
        this.restaurantTable = restaurantTable;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }
}
