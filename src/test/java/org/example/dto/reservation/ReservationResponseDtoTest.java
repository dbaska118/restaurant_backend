package org.example.dto.reservation;

import org.example.model.reservation.ReservationStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class ReservationResponseDtoTest {

    @Test
    public void setTest(){
        LocalDateTime now = LocalDateTime.now();
        ReservationResponseDto reservationResponseDto = new ReservationResponseDto();
        reservationResponseDto.setId(1L);
        reservationResponseDto.setEmail("test@test.com");
        reservationResponseDto.setTableName("Stolik 1");
        reservationResponseDto.setStartTime(now);
        reservationResponseDto.setEndTime(now.plusHours(2));
        reservationResponseDto.setPrice(150);
        reservationResponseDto.setReservationCode("001235");
        reservationResponseDto.setReservationStatus(ReservationStatus.CONFIRMED);

        Assertions.assertEquals(1L, reservationResponseDto.getId());
        Assertions.assertEquals("test@test.com", reservationResponseDto.getEmail());
        Assertions.assertEquals("Stolik 1", reservationResponseDto.getTableName());
        Assertions.assertEquals(now, reservationResponseDto.getStartTime());
        Assertions.assertEquals(now.plusHours(2), reservationResponseDto.getEndTime());
        Assertions.assertEquals(150, reservationResponseDto.getPrice());
        Assertions.assertEquals("001235", reservationResponseDto.getReservationCode());
        Assertions.assertEquals(ReservationStatus.CONFIRMED, reservationResponseDto.getReservationStatus());
    }
}
