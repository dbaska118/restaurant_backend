package org.example.dto.reservation;


import org.example.model.reservation.RestaurantTableStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RestaurantTableStatusRequestTest {

    @Test
    public void getTest(){
        RestaurantTableStatusRequest request = new RestaurantTableStatusRequest(1L, 0, RestaurantTableStatus.FREE);
        Assertions.assertEquals(1L, request.getId());
        Assertions.assertEquals(0, request.getVersion());
        Assertions.assertEquals(RestaurantTableStatus.FREE, request.getStatus());
    }

    @Test
    public void setTest(){
        RestaurantTableStatusRequest request = new RestaurantTableStatusRequest();
        request.setId(5L);
        request.setVersion(2);
        request.setStatus(RestaurantTableStatus.OCCUPIED);

        Assertions.assertEquals(5L, request.getId());
        Assertions.assertEquals(2, request.getVersion());
        Assertions.assertEquals(RestaurantTableStatus.OCCUPIED, request.getStatus());
    }
}
