package org.example.dto.reservation;

import org.example.model.reservation.BalanceOperation;
import org.example.model.reservation.BalanceOperationType;
import org.example.model.user.Client;
import org.example.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

public class BalanceOperationMapperTest {


    private final BalanceOperationMapper mapper = new BalanceOperationMapper();

    @Test
    public void fromBalanceOperationTest() {
        LocalDateTime now = LocalDateTime.now();
        User client = new Client("client@wp.pl", "password", "Jan", "Nowak");
        BalanceOperation balanceOperation = new BalanceOperation(client, now, 50, 200, 250, BalanceOperationType.ADD_FUNDS);
        ReflectionTestUtils.setField(balanceOperation, "id", 123L);

        BalanceOperationDTO dto = mapper.fromBalanceOperation(balanceOperation);

        Assertions.assertEquals(123L, dto.getId());
        Assertions.assertEquals("client@wp.pl", dto.getEmail());
        Assertions.assertEquals(now, dto.getOperationDate());
        Assertions.assertEquals(50, dto.getAmount());
        Assertions.assertEquals(200, dto.getBalanceBefore());
        Assertions.assertEquals(250, dto.getBalanceAfter());
        Assertions.assertEquals(BalanceOperationType.ADD_FUNDS, dto.getOperationType());
    }

}
