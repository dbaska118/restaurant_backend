package org.example.service.reservation;

import jakarta.persistence.EntityManager;
import org.example.exception.NotClientException;
import org.example.exception.UserNotFoundException;
import org.example.model.reservation.BalanceOperation;
import org.example.model.reservation.BalanceOperationType;
import org.example.model.user.Admin;
import org.example.model.user.Client;
import org.example.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BalanceOperationServiceTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BalanceOperationService balanceOperationService;

    @Test
    public void getByEmailTest(){
        User client = new Client("user@wp.pl", "password", "Jan", "Nowak");
        User admin = new Admin("admin@wp.pl", "password", "Tomasz", "Kowal");
        entityManager.persist(client);
        entityManager.persist(admin);

        LocalDateTime now = LocalDateTime.now();
        BalanceOperation balanceOperation = new BalanceOperation(
                client,
                now,
                -50,
                100,
                50,
                BalanceOperationType.ADD_FUNDS
        );
        entityManager.persist(balanceOperation);

        List<BalanceOperation> balanceOperationList = balanceOperationService.geAllOperationsByEmail(client.getEmail());
        Assertions.assertEquals(1, balanceOperationList.size());
        Assertions.assertEquals(balanceOperation, balanceOperationList.get(0));

        Assertions.assertThrows(UserNotFoundException.class, ()->{
            balanceOperationService.geAllOperationsByEmail("test@wp.pl");
        });

        Assertions.assertThrows(NotClientException.class, ()->{
            balanceOperationService.geAllOperationsByEmail(admin.getEmail());
        });

    }

}
