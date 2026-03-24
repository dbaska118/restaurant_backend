package org.example.dto.user;

public class ClientDtoResponse extends UserDtoResponse {

    private double balance;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
