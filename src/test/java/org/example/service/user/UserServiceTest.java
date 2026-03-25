package org.example.service.user;

import jakarta.persistence.EntityManager;
import org.example.dto.user.ChangePasswordRequest;
import org.example.exception.InvalidPasswordException;
import org.example.exception.NotClientException;
import org.example.exception.UserNotFoundException;
import org.example.model.user.Admin;
import org.example.model.user.Client;
import org.example.model.user.Employee;
import org.example.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserServiceTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Test
    public void getBalanceTest(){
        User client = new Client("client@wp.pl", "password", "Jan", "Nowak");
        ((Client)client).setBalance(123);
        User admin = new Admin("admin@wp.pl", "password", "Tomasz", "Kowalski");
        entityManager.persist(client);
        entityManager.persist(admin);

        double balance = userService.getBalance("client@wp.pl");
        Assertions.assertEquals(123, balance);

        Assertions.assertThrows(NotClientException.class, () -> {
            userService.getBalance("admin@wp.pl");
        });

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.getBalance("employee@wp.pl");
        });
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

        userService.changePassword(changePasswordRequest);
        User clientDB = entityManager.find(User.class, client.getId());
        Assertions.assertTrue(passwordEncoder.matches(changePasswordRequest.getNewPassword(), clientDB.getPassword()));

        Assertions.assertThrows(InvalidPasswordException.class, () -> {
            changePasswordRequest.setOldPassword("123456");
            userService.changePassword(changePasswordRequest);
        });

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            changePasswordRequest.setEmail("employee@wp.pl");
            userService.changePassword(changePasswordRequest);
        });
    }

    @Test
    public void getAllUsersAdmin(){
        User client = new Client("client@wp.pl", "password", "Jan", "Nowak");
        User employee = new Employee("employee@wp.pl", "password", "Michał", "Kowal");
        User admin = new Admin("admin@wp.pl", "password", "Tomasz", "Kowalski");

        entityManager.persist(client);
        entityManager.persist(employee);
        entityManager.persist(admin);

        List<User> users = userService.getAllUsersAdmin();
        Assertions.assertEquals(2, users.size());
        Assertions.assertEquals(client, users.get(0));
        Assertions.assertEquals(employee, users.get(1));
    }

    @Test
    public void getAllUsers(){
        User client = new Client("client@wp.pl", "password", "Jan", "Nowak");
        User employee = new Employee("employee@wp.pl", "password", "Michał", "Kowal");
        User admin = new Admin("admin@wp.pl", "password", "Tomasz", "Kowalski");

        entityManager.persist(client);
        entityManager.persist(employee);
        entityManager.persist(admin);

        List<User> users = userService.getAllUsers();
        Assertions.assertEquals(3, users.size());
        Assertions.assertEquals(client, users.get(0));
        Assertions.assertEquals(employee, users.get(1));
        Assertions.assertEquals(admin, users.get(2));
    }
}
