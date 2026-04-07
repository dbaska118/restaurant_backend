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

    public RestaurantTable() {
        this.active = true;
    }

    public RestaurantTable(String name, int numberOfChairs) {
        this.active = true;
        this.name = name;
        this.numberOfChairs = numberOfChairs;
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
}
