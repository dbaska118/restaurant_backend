package org.example.dto.reservation;

import org.example.model.reservation.BalanceOperationType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class BalanceOperationDtoTest {

    @Test
    public void getTest(){
        LocalDateTime now = LocalDateTime.now();
        BalanceOperationDTO balanceOperationDTO = new BalanceOperationDTO(
                1L,
                "client@wp.pl",
                now,
                50,
                100,
                150,
                BalanceOperationType.ADD_FUNDS
        );

        Assertions.assertEquals(1L, balanceOperationDTO.getId());
        Assertions.assertEquals("client@wp.pl", balanceOperationDTO.getEmail());
        Assertions.assertEquals(now, balanceOperationDTO.getOperationDate());
        Assertions.assertEquals(50, balanceOperationDTO.getAmount());
        Assertions.assertEquals(100, balanceOperationDTO.getBalanceBefore());
        Assertions.assertEquals(150, balanceOperationDTO.getBalanceAfter());
        Assertions.assertEquals(BalanceOperationType.ADD_FUNDS, balanceOperationDTO.getOperationType());
    }
}
