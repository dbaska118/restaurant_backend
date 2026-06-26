package org.example.model.balance;

import jakarta.persistence.*;
import org.example.model.user.User;

import java.time.LocalDateTime;

@Entity
public class BalanceOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private LocalDateTime operationDate;

    private double amount;

    private double balanceBefore;

    private double balanceAfter;

    private BalanceOperationType operationType;

    public BalanceOperation() {
    }

    public BalanceOperation(User user, LocalDateTime operationDate, double amount, double balanceBefore, double balanceAfter, BalanceOperationType operationType) {
        this.user = user;
        this.operationDate = operationDate;
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.operationType = operationType;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
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
