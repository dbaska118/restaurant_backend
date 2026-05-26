package org.example.dto.reservation;

import org.example.model.reservation.ReservationStatus;

import java.time.LocalDateTime;

public class ReservationResponseDto {

    private Long id;

    private String email;

    private String tableName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private double price;

    private String reservationCode;

    private ReservationStatus reservationStatus;

    private int NumberOfChairs;

    public ReservationResponseDto() {
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getTableName() {
        return tableName;
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

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public int getNumberOfChairs() {
        return NumberOfChairs;
    }

    public void setNumberOfChairs(int numberOfChairs) {
        NumberOfChairs = numberOfChairs;
    }
}
