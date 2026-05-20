package org.example.dto.reservation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class ReservationPublicDtoTest {

    @Test
    public void setTest(){
        LocalDateTime now = LocalDateTime.now();
        ReservationPublicDto reservationPublicDto = new ReservationPublicDto();
        reservationPublicDto.setTableId(2L);
        reservationPublicDto.setStartTime(now);
        reservationPublicDto.setEndTime(now.plusHours(2));

        Assertions.assertEquals(reservationPublicDto.getTableId(), 2L);
        Assertions.assertEquals(reservationPublicDto.getStartTime(), now);
        Assertions.assertEquals(reservationPublicDto.getEndTime(), now.plusHours(2));
    }
}
