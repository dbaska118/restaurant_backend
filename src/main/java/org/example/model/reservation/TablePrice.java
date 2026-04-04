package org.example.model.reservation;
import jakarta.persistence.*;

@Entity
public class TablePrice {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    public Long getId() {
        return id;
    }

    @Column(unique = true, nullable = false)
    private int numberOfChairs;

    private double price;

    public TablePrice() {
    }

    public TablePrice(int numberOfChairs, double price) {
        this.numberOfChairs = numberOfChairs;
        this.price = price;
    }

    public int getNumberOfChairs() {
        return numberOfChairs;
    }

    public double getPrice() {
        return price;
    }

    public void setNumberOfChairs(int numberOfChairs) {
        this.numberOfChairs = numberOfChairs;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
