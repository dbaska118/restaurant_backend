package org.example.service.user;

import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.EntityManager;
import org.example.dto.user.ChangeNameRequest;
import org.example.dto.user.ChangePasswordRequest;
import org.example.dto.user.NameResponse;
import org.example.exception.*;
import org.example.model.user.Admin;
import org.example.model.user.Client;
import org.example.model.user.Employee;
import org.example.model.user.User;
import org.hibernate.boot.model.source.spi.AssociationSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Test
    public void deleteUserTest(){
        User employee = new Employee("employee@wp.pl", "password", "Michał", "Kowal");
        User admin = new Admin("admin@wp.pl", "password", "Tomasz", "Kowalski");
        entityManager.persist(employee);
        entityManager.persist(admin);

        userService.deleteUser(employee.getId());
        List<User> users = entityManager.createQuery("select u from User u", User.class).getResultList();
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals(admin, users.get(0));

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(-1L);
        });

        Assertions.assertThrows(IsAdminException.class, () -> {
            userService.deleteUser(admin.getId());
        });
    }

    @Test
    public void deleteAdminTest(){
        User employee = new Employee("employee@wp.pl", "password", "Michał", "Kowal");
        User admin = new Admin("admin@wp.pl", "password", "Tomasz", "Kowalski");
        entityManager.persist(employee);
        entityManager.persist(admin);

        userService.deleteAdmin(admin.getId());
        List<User> users = entityManager.createQuery("select u from User u", User.class).getResultList();
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals(employee, users.get(0));

        Assertions.assertThrows(NotAdminException.class, () -> {
            userService.deleteAdmin(employee.getId());
        });

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.deleteAdmin(-1L);
        });
    }

    @Test
    public void updateUserTest(){
        User employee = new Employee("employee@wp.pl", "password", "Michał", "Kowal");
        User admin = new Admin("admin@wp.pl", "password", "Tomasz", "Kowalski");
        entityManager.persist(employee);
        entityManager.persist(admin);

        employee.setFirstName("Anna");
        employee.setLastName("Kowalska");
        employee.setPassword("haslo");

        User userdb = userService.updateUser(employee.getId(), employee);
        Assertions.assertEquals("Anna", userdb.getFirstName());
        Assertions.assertEquals("Kowalska", userdb.getLastName());
        Assertions.assertTrue(passwordEncoder.matches("haslo", userdb.getPassword()));

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(-1L, employee);
        });

        Assertions.assertThrows(IsAdminException.class, () -> {
            userService.updateUser(admin.getId(), admin);
        });
    }

    @Test
    public void updateAdminTest(){
        User employee = new Employee("employee@wp.pl", "password", "Michał", "Kowal");
        User admin = new Admin("admin@wp.pl", "password", "Tomasz", "Kowalski");
        entityManager.persist(employee);
        entityManager.persist(admin);

        admin.setFirstName("Anna");
        admin.setLastName("Kowalska");
        admin.setPassword("haslo");

        User adminDB = userService.updateAdmin(admin.getId(), admin);
        Assertions.assertEquals("Anna", adminDB.getFirstName());
        Assertions.assertEquals("Kowalska", adminDB.getLastName());
        Assertions.assertTrue(passwordEncoder.matches("haslo", adminDB.getPassword()));

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.updateAdmin(-1L, admin);
        });

        Assertions.assertThrows(NotAdminException.class, () -> {
            userService.updateAdmin(employee.getId(), employee);
        });
    }

    @Test
    public void changeNameTest(){
        User employee = new Employee("employee@wp.pl", "password", "Michał", "Kowal");
        entityManager.persist(employee);

        ChangeNameRequest request = new ChangeNameRequest();
        request.setFirstName("Anna");
        request.setLastName("Kowalska");
        request.setEmail("employee@wp.pl");

        userService.changeName(request);
        User employeeDB = entityManager.find(User.class, employee.getId());

        Assertions.assertEquals("Anna", employeeDB.getFirstName());
        Assertions.assertEquals("Kowalska", employeeDB.getLastName());
    }

    @Test
    public void getNameTest(){
        User employee = new Employee("employee@wp.pl", "password", "Michał", "Kowal");
        entityManager.persist(employee);

        NameResponse nameResponse = userService.getName(employee.getEmail());
        Assertions.assertEquals("Michał", nameResponse.getFirstName());
        Assertions.assertEquals("Kowal", nameResponse.getLastName());

        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.getName("test@wp.pl");
        });
    }
}
