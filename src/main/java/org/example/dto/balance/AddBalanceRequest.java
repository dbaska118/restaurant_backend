package org.example.dto.balance;

public class AddBalanceRequest {

    private String email;

    private double amount;

    public AddBalanceRequest(String email, double amount) {
        this.email = email;
        this.amount = amount;
    }


    public String getEmail() {
        return email;
    }

    public double getAmount() {
        return amount;
    }
}
