package org.example.repository;

import org.example.model.user.Admin;
import org.example.model.user.User;
import org.example.repository.user.UserRepository;
import org.example.service.dish.DishService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByEmailTest(){
        User admin = new Admin("user@wp.pl", "password", "Jan", "Nowak");
        User admin2 = new Admin("admin@wp.pl", "password", "Tomasz", "Kowalski");

        entityManager.persist(admin);
        entityManager.persist(admin2);

        Optional<User> user = userRepository.findByEmail(admin.getEmail());
        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals(user.get(), admin);

        user = userRepository.findByEmail("email@wp.pl");
        Assertions.assertTrue(user.isEmpty());


    }
}
