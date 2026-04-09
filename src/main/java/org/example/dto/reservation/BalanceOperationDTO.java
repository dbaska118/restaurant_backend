package org.example.dto.reservation;

import org.example.model.reservation.BalanceOperationType;

import java.time.LocalDateTime;

public class BalanceOperationDTO {

    private long id;

    private String email;

    private LocalDateTime operationDate;

    private double amount;

    private double balanceBefore;

    private double balanceAfter;

    private BalanceOperationType operationType;

    public BalanceOperationDTO(long id, String email, LocalDateTime operationDate, double amount, double balanceBefore, double balanceAfter, BalanceOperationType operationType) {
        this.id = id;
        this.email = email;
        this.operationDate = operationDate;
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.operationType = operationType;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getOperationDate() {
        return operationDate;
    }

    public double getAmount() {
        return amount;
    }

    public double getBalanceBefore() {
        return balanceBefore;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public BalanceOperationType getOperationType() {
        return operationType;
    }
}
