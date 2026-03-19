package org.example.controller.user;

import jakarta.persistence.EntityManager;
import org.example.controller.dish.DishController;
import org.example.model.user.Admin;
import org.example.model.user.Client;
import org.example.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AuthControllerTest {

    @Autowired
    private AuthController authController;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void registerTest(){
        User admin = new Admin("admin@wp.pl", "password", "Jan", "Nowak");
        User client = new Client("client@wp.pl", "password", "Tomasz", "Nowak");
        User client2 = new Client("client@wp.pl", "haslo123", "Daniel", "Kowalski");

        ResponseEntity<?> response = authController.register(client);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        response = authController.register(client2);
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        response = authController.register(admin);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }
}
