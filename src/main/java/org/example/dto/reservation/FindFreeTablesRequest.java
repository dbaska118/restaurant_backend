package org.example.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class FindFreeTablesRequest {
    private int minNumberOfChairs;

    private LocalDate reservationDay;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime reservationStartTime;

    private int reservationLength;

    public int getMinNumberOfChairs() {
        return minNumberOfChairs;
    }

    public LocalDate getReservationDay() {
        return reservationDay;
    }

    public LocalTime getReservationStartTime() {
        return reservationStartTime;
    }

    public int getReservationLength() {
        return reservationLength;
    }

    public void setMinNumberOfChairs(int minNumberOfChairs) {
        this.minNumberOfChairs = minNumberOfChairs;
    }

    public void setReservationDay(LocalDate reservationDay) {
        this.reservationDay = reservationDay;
    }

    public void setReservationStartTime(LocalTime reservationStartTime) {
        this.reservationStartTime = reservationStartTime;
    }

    public void setReservationLength(int reservationLength) {
        this.reservationLength = reservationLength;
    }
}
