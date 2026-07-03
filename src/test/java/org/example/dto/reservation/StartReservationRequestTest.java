package org.example.dto.reservation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StartReservationRequestTest {

    @Test
    public void setTest(){
        StartReservationRequest request = new StartReservationRequest();
        request.setReservationId(1L);
        request.setTableId(2L);
        request.setReservationCode("123456");
        request.setVersion(5);

        Assertions.assertEquals(1L, request.getReservationId());
        Assertions.assertEquals(2L, request.getTableId());
        Assertions.assertEquals("123456", request.getReservationCode());
        Assertions.assertEquals(5, request.getVersion());
    }
}
