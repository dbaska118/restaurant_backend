package org.example.model.reservation;

import jakarta.persistence.*;

@Entity
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int numberOfChairs;

    private boolean active;

    private RestaurantTableStatus status;

    @Version
    private Integer version;

    public RestaurantTable() {
        this.active = true;
        this.status = RestaurantTableStatus.FREE;
    }

    public RestaurantTable(String name, int numberOfChairs) {
        this.active = true;
        this.name = name;
        this.numberOfChairs = numberOfChairs;
        this.status = RestaurantTableStatus.FREE;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfChairs() {
        return numberOfChairs;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOfChairs(int numberOfChairs) {
        this.numberOfChairs = numberOfChairs;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public RestaurantTableStatus getStatus() {
        return status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setStatus(RestaurantTableStatus status) {
        this.status = status;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
