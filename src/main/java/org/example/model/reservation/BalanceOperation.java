package org.example.model.reservation;

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

    private double balance_before;

    private double balance_after;

    private BalanceOperationType operationType;

    public BalanceOperation() {
    }

    public BalanceOperation(User user, LocalDateTime operationDate, double amount, double balance_before, double balance_after, BalanceOperationType operationType) {
        this.user = user;
        this.operationDate = operationDate;
        this.amount = amount;
        this.balance_before = balance_before;
        this.balance_after = balance_after;
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

    public double getBalance_before() {
        return balance_before;
    }

    public double getBalance_after() {
        return balance_after;
    }

    public BalanceOperationType getOperationType() {
        return operationType;
    }
}
