package org.example.model.reservation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class OpeningHoursTest {

    @Test
    public void getTest(){
        LocalTime startTime = LocalTime.of(8,0);
        LocalTime endTime = LocalTime.of(20,0);
        OpeningHours openingHours = new OpeningHours("MONDAY", 1, startTime, endTime);

        Assertions.assertEquals("MONDAY", openingHours.getDayOfWeek());
        Assertions.assertEquals(1, openingHours.getDayOrder());
        Assertions.assertEquals(LocalTime.of(8,0), openingHours.getOpenTime());
        Assertions.assertEquals(LocalTime.of(20,0), openingHours.getCloseTime());
    }

    @Test
    public void setTest(){
        LocalTime startTime = LocalTime.of(8,0);
        LocalTime endTime = LocalTime.of(20,0);
        OpeningHours openingHours = new OpeningHours();
        openingHours.setDayOfWeek("MONDAY");
        openingHours.setDayOrder(1);
        openingHours.setOpenTime(startTime);
        openingHours.setCloseTime(endTime);

        Assertions.assertEquals("MONDAY", openingHours.getDayOfWeek());
        Assertions.assertEquals(1, openingHours.getDayOrder());
        Assertions.assertEquals(LocalTime.of(8,0), openingHours.getOpenTime());
        Assertions.assertEquals(LocalTime.of(20,0), openingHours.getCloseTime());
    }
}
