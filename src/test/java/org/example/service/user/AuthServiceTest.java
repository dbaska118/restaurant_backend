package org.example.service.user;

import jakarta.persistence.EntityManager;
import org.example.model.user.Admin;
import org.example.model.user.User;
import org.example.service.dish.DishService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void registerTest(){
        User admin = new Admin("user@wp.pl", "password", "Jan", "Nowak");
        User admin2 = new Admin("user@wp.pl", "haslo", "Tomasz", "Kowalski");

        authService.register(admin);
        List<User> userList = entityManager.createQuery("select u from User u").getResultList();
        Assertions.assertEquals(1, userList.size());
        Assertions.assertEquals(userList.get(0), admin);


        Assertions.assertThrows(RuntimeException.class, () -> authService.register(admin2));
    }
}
