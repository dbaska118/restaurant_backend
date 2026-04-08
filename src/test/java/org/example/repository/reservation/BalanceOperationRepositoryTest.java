package org.example.repository.reservation;


import org.example.model.reservation.BalanceOperation;
import org.example.model.reservation.BalanceOperationType;
import org.example.model.user.Admin;
import org.example.model.user.Client;
import org.example.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class BalanceOperationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BalanceOperationRepository balanceOperationRepository;

    @Test
    public void findByUserTest(){
        User user = new Client("user@wp.pl", "password", "Jan", "Nowak");
        User admin = new Admin("admin@wp.pl", "password", "Tomasz", "Kowalski");
        entityManager.persist(user);
        entityManager.persist(admin);
        LocalDateTime now = LocalDateTime.now();
        BalanceOperation balanceOperation = new BalanceOperation(
                user,
                now,
                -50,
                100,
                50,
                BalanceOperationType.ADD_FUNDS
        );
        entityManager.persist(balanceOperation);

        List<BalanceOperation> balanceOperationList = balanceOperationRepository.findAllByUser(user);
        Assertions.assertEquals(1, balanceOperationList.size());

        balanceOperationList = balanceOperationRepository.findAllByUser(admin);
        Assertions.assertEquals(0, balanceOperationList.size());
    }
}
