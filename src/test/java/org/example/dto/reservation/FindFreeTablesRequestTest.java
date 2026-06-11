package org.example.dto.reservation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

public class FindFreeTablesRequestTest {

    @Test
    public void setTest(){
        LocalDate today = LocalDate.now();
        LocalTime hour = LocalTime.now();
        FindFreeTablesRequest request = new FindFreeTablesRequest();
        request.setMinNumberOfChairs(5);
        request.setReservationDay(today);
        request.setReservationStartTime(hour);
        request.setReservationLength(2);

        Assertions.assertEquals(5, request.getMinNumberOfChairs());
        Assertions.assertEquals(today, request.getReservationDay());
        Assertions.assertEquals(hour, request.getReservationStartTime());
        Assertions.assertEquals(2, request.getReservationLength());
    }
}
