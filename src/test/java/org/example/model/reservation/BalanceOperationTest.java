package org.example.model.reservation;

import org.example.model.user.Client;
import org.example.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class BalanceOperationTest {

    @Test
    public void getTest(){
        User client = new Client("client@wp.pl", "password", "Jan", "Nowak");
        LocalDateTime now = LocalDateTime.now();
        BalanceOperation balanceOperation = new BalanceOperation(
                client,
                now,
                -50,
                100,
                50,
                BalanceOperationType.ADD_FUNDS
        );

        Assertions.assertEquals(client, balanceOperation.getUser());
        Assertions.assertEquals(now, balanceOperation.getOperationDate());
        Assertions.assertEquals(-50, balanceOperation.getAmount());
        Assertions.assertEquals(100, balanceOperation.getBalance_before());
        Assertions.assertEquals(50, balanceOperation.getBalance_after());
        Assertions.assertEquals(BalanceOperationType.ADD_FUNDS, balanceOperation.getOperationType());
    }
}
