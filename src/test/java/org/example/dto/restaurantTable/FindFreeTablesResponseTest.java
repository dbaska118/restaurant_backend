package org.example.dto.restaurantTable;

import org.example.model.restaurantTable.RestaurantTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FindFreeTablesResponseTest {

    @Test
    public void setTest(){
        RestaurantTable table1 = new RestaurantTable();
        List<RestaurantTable> exactTables = new ArrayList<>();
        exactTables.add(table1);
        RestaurantTable table2 = new RestaurantTable();
        List<RestaurantTable> earlierTables = new ArrayList<>();
        earlierTables.add(table2);
        RestaurantTable table3 = new RestaurantTable();
        List<RestaurantTable> laterTables = new ArrayList<>();
        laterTables.add(table3);
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(2);

        FindFreeTablesResponse response = new FindFreeTablesResponse();
        response.setStartTime(startTime);
        response.setEndTime(endTime);
        response.setExactTables(exactTables);
        response.setEarlierTables(earlierTables);
        response.setLaterTables(laterTables);

        Assertions.assertEquals(startTime, response.getStartTime());
        Assertions.assertEquals(endTime, response.getEndTime());
        Assertions.assertEquals(exactTables, response.getExactTables());
        Assertions.assertEquals(table1, response.getExactTables().get(0));
        Assertions.assertEquals(earlierTables, response.getEarlierTables());
        Assertions.assertEquals(table2, response.getEarlierTables().get(0));
        Assertions.assertEquals(laterTables, response.getLaterTables());
        Assertions.assertEquals(table3, response.getLaterTables().get(0));
    }
}
