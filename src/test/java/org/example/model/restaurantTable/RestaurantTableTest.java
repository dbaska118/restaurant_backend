package org.example.model.restaurantTable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RestaurantTableTest {

    @Test
    public void getTest(){
        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 2);
        Assertions.assertEquals("Stolik 1", restaurantTable.getName());
        Assertions.assertEquals(2, restaurantTable.getNumberOfChairs());
        Assertions.assertTrue(restaurantTable.getActive());
        Assertions.assertEquals(RestaurantTableStatus.FREE, restaurantTable.getStatus());
        Assertions.assertEquals(null, restaurantTable.getVersion());
    }

    @Test
    public void setTest(){
        RestaurantTable restaurantTable = new RestaurantTable();
        restaurantTable.setName("Stół 2");
        restaurantTable.setNumberOfChairs(4);
        restaurantTable.setActive(false);
        restaurantTable.setStatus(RestaurantTableStatus.OCCUPIED);
        restaurantTable.setVersion(1);

        Assertions.assertEquals("Stół 2", restaurantTable.getName());
        Assertions.assertEquals(4, restaurantTable.getNumberOfChairs());
        Assertions.assertFalse(restaurantTable.getActive());
        Assertions.assertEquals(RestaurantTableStatus.OCCUPIED, restaurantTable.getStatus());
        Assertions.assertEquals(1, restaurantTable.getVersion());
    }
}
