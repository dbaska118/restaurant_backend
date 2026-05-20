package org.example.dto.reservation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class ReservationRequestDtoTest {

    @Test
    public void setTest(){
        LocalDateTime now = LocalDateTime.now();
        ReservationRequestDto reservationRequestDTO = new ReservationRequestDto();
        reservationRequestDTO.setEmail("test@test.com");
        reservationRequestDTO.setTableId(1);
        reservationRequestDTO.setStartTime(now);
        reservationRequestDTO.setEndTime(now.plusHours(2));

        Assertions.assertEquals("test@test.com", reservationRequestDTO.getEmail());
        Assertions.assertEquals(1, reservationRequestDTO.getTableId());
        Assertions.assertEquals(now, reservationRequestDTO.getStartTime());
        Assertions.assertEquals(now.plusHours(2), reservationRequestDTO.getEndTime());
    }
}
