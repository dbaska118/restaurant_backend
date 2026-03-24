package org.example.controller.user;


import jakarta.persistence.EntityManager;
import org.example.dto.user.ChangePasswordRequest;
import org.example.exception.UserNotFoundException;
import org.example.model.user.Admin;
import org.example.model.user.Client;
import org.example.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EntityManager entityManager;

    @Test
    public void getBalanceTest(){
        User client = new Client("client@wp.pl", "password", "Jan", "Nowak");
        ((Client)client).setBalance(123);
        User admin = new Admin("admin@wp.pl", "password", "Tomasz", "Kowalski");
        entityManager.persist(client);
        entityManager.persist(admin);

        ResponseEntity<Double> response = userController.getBalance(client.getEmail());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        response = userController.getBalance(admin.getEmail());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        response = userController.getBalance("email@wp.pl");
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void changePasswordTest(){
        User client = new Client("client@wp.pl", "password", "Jan", "Nowak");
        client.setPassword(passwordEncoder.encode("password"));
        entityManager.persist(client);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setOldPassword("password");
        changePasswordRequest.setNewPassword("haslo");
        changePasswordRequest.setEmail("client@wp.pl");

        ResponseEntity<?> response = userController.changePassword(changePasswordRequest);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        changePasswordRequest.setOldPassword("123456");
        response = userController.changePassword(changePasswordRequest);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        changePasswordRequest.setEmail("email@wp.pl");
        response = userController.changePassword(changePasswordRequest);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
