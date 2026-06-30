package org.example.dto.restaurantTable;

import org.example.model.restaurantTable.RestaurantTableStatus;

public class RestaurantTableReservationDto {

    private Long id;

    private String name;

    private int numberOfChairs;

    private RestaurantTableStatus status;

    private Integer version;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfChairs() {
        return numberOfChairs;
    }

    public RestaurantTableStatus getStatus() {
        return status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOfChairs(int numberOfChairs) {
        this.numberOfChairs = numberOfChairs;
    }

    public void setStatus(RestaurantTableStatus status) {
        this.status = status;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
