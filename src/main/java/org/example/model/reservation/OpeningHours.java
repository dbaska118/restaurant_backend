package org.example.model.reservation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalTime;

@Entity
public class OpeningHours {

    @Id
    private String DayOfWeek;
    private int dayOrder;
    private LocalTime openTime;
    private LocalTime closeTime;

    public OpeningHours(String dayOfWeek, int dayOrder, LocalTime openTime, LocalTime closeTime) {
        DayOfWeek = dayOfWeek;
        this.dayOrder = dayOrder;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public OpeningHours() {

    }

    public void setDayOfWeek(String dayOfWeek) {
        DayOfWeek = dayOfWeek;
    }

    public void setOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public void setCloseTime(LocalTime closeTime) {
        this.closeTime = closeTime;
    }

    public String getDayOfWeek() {
        return DayOfWeek;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }

    public int getDayOrder() {
        return dayOrder;
    }

    public void setDayOrder(int dayOrder) {
        this.dayOrder = dayOrder;
    }
}
