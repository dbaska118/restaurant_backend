package org.example.dto.reservation;

import org.example.dto.restaurantTable.RestaurantTableReservationDto;
import org.example.model.reservation.ReservationStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class ReservationWithTableDtoTest {

    @Test
    public void setTest(){
        RestaurantTableReservationDto restaurantTableReservationDto = new RestaurantTableReservationDto();
        LocalDateTime now = LocalDateTime.now();

        ReservationWithTableDto reservationWithTableDto = new ReservationWithTableDto();
        reservationWithTableDto.setId(1L);
        reservationWithTableDto.setEmail("test@test.com");
        reservationWithTableDto.setStartTime(now);
        reservationWithTableDto.setEndTime(now.plusHours(2));
        reservationWithTableDto.setReservationStatus(ReservationStatus.CONFIRMED);
        reservationWithTableDto.setRestaurantTableReservationDTO(restaurantTableReservationDto);

        Assertions.assertEquals(1L, reservationWithTableDto.getId());
        Assertions.assertEquals("test@test.com", reservationWithTableDto.getEmail());
        Assertions.assertEquals(now, reservationWithTableDto.getStartTime());
        Assertions.assertEquals(now.plusHours(2), reservationWithTableDto.getEndTime());
        Assertions.assertEquals(ReservationStatus.CONFIRMED, reservationWithTableDto.getReservationStatus());
        Assertions.assertEquals(restaurantTableReservationDto, reservationWithTableDto.getRestaurantTableReservationDTO());

    }
}
