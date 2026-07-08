package org.example.dto.reservation;


import org.example.model.reservation.ReservationStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class NextReservationDtoTest {

    @Test
    public void setTest(){
        LocalDateTime now = LocalDateTime.now();
        NextReservationDTO dto = new NextReservationDTO();
        dto.setId(2L);
        dto.setTableId(5L);
        dto.setStartTime(now);
        dto.setEndTime(now.plusHours(2));
        dto.setStatus(ReservationStatus.CONFIRMED);

        Assertions.assertEquals(2L, dto.getId());
        Assertions.assertEquals(5L, dto.getTableId());
        Assertions.assertEquals(now, dto.getStartTime());
        Assertions.assertEquals(now.plusHours(2), dto.getEndTime());
        Assertions.assertEquals(ReservationStatus.CONFIRMED, dto.getStatus());
    }
}
