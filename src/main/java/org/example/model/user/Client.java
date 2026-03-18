package org.example.model.user;

import jakarta.persistence.Entity;

@Entity
public class Client extends User{

    private double balance;

    public Client() {
        this.setRole("client");
        this.balance = 0;
    }

    public Client(String email, String password, String firstName, String lastName) {
        super(email, password, firstName, lastName);
        this.balance = 0;
        this.setRole("client");
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
