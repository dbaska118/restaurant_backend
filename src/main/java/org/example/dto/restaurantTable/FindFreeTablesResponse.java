package org.example.dto.restaurantTable;

import org.example.model.restaurantTable.RestaurantTable;

import java.time.LocalDateTime;
import java.util.List;

public class FindFreeTablesResponse {

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private List<RestaurantTable> exactTables;
    private List<RestaurantTable> earlierTables;
    private List<RestaurantTable> laterTables;

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public List<RestaurantTable> getExactTables() {
        return exactTables;
    }

    public List<RestaurantTable> getEarlierTables() {
        return earlierTables;
    }

    public List<RestaurantTable> getLaterTables() {
        return laterTables;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setExactTables(List<RestaurantTable> exactTables) {
        this.exactTables = exactTables;
    }

    public void setEarlierTables(List<RestaurantTable> earlierTables) {
        this.earlierTables = earlierTables;
    }

    public void setLaterTables(List<RestaurantTable> laterTables) {
        this.laterTables = laterTables;
    }
}
