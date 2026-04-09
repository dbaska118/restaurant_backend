package org.example.dto.reservation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

public class OpeningHoursRequestTest {

    @Test
    public void getTest(){
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(20, 0);
        OpeningHoursRequest openingHoursRequest = new OpeningHoursRequest(startTime, endTime);

        Assertions.assertEquals(startTime, openingHoursRequest.getOpenTime());
        Assertions.assertEquals(endTime, openingHoursRequest.getCloseTime());
    }

    @Test
    public void setTest(){
        OpeningHoursRequest openingHoursRequest = new OpeningHoursRequest(LocalTime.of(8, 0), LocalTime.of(20, 0));

        LocalTime newStartTime = LocalTime.of(10, 0);
        LocalTime newEndTime = LocalTime.of(16, 0);

        openingHoursRequest.setOpenTime(newStartTime);
        openingHoursRequest.setCloseTime(newEndTime);

        Assertions.assertEquals(newStartTime, openingHoursRequest.getOpenTime());
        Assertions.assertEquals(newEndTime, openingHoursRequest.getCloseTime());
    }
}
