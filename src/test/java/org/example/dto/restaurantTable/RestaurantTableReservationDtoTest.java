package org.example.dto.restaurantTable;

import org.example.model.restaurantTable.RestaurantTableStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RestaurantTableReservationDtoTest {

    @Test
    public void setTest(){
        RestaurantTableReservationDto restaurantTableReservationDto = new RestaurantTableReservationDto();
        restaurantTableReservationDto.setId(1L);
        restaurantTableReservationDto.setName("Stolik 1");
        restaurantTableReservationDto.setNumberOfChairs(5);
        restaurantTableReservationDto.setStatus(RestaurantTableStatus.FREE);
        restaurantTableReservationDto.setVersion(2);

        Assertions.assertEquals(1L, restaurantTableReservationDto.getId());
        Assertions.assertEquals("Stolik 1", restaurantTableReservationDto.getName());
        Assertions.assertEquals(5, restaurantTableReservationDto.getNumberOfChairs());
        Assertions.assertEquals(RestaurantTableStatus.FREE, restaurantTableReservationDto.getStatus());
        Assertions.assertEquals(2, restaurantTableReservationDto.getVersion());
    }
}
