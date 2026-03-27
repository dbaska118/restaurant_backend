package org.example.controller.user;


import jakarta.persistence.EntityManager;
import org.example.dto.user.ChangeNameRequest;
import org.example.dto.user.ChangePasswordRequest;
import org.example.dto.user.NameResponse;
import org.example.dto.user.UserDtoResponse;
import org.example.exception.UserNotFoundException;
import org.example.model.user.Admin;
import org.example.model.user.Client;
import org.example.model.user.Employee;
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

import java.security.Principal;

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

    @Test
    public void createUserTest(){
        User client = new Client("client@wp.pl", "password", "Jan", "Nowak");
        client.setRole("test");
        User employee = new Employee("employee@wp.pl", "password", "Anna", "Kowalska");
        employee.setRole("test");
        User admin = new Admin("admin@wp.pl", "password", "Tomasz", "Kowalski");

        ResponseEntity<UserDtoResponse> response = userController.addUser(client);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals("client", response.getBody().getRole());

        response = userController.addUser(employee);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals("employee", response.getBody().getRole());

        response = userController.addUser(admin);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        response = userController.addUser(employee);
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void createAdminTest(){
        User client = new Client("client@wp.pl", "password", "Jan", "Nowak");
        User employee = new Employee("employee@wp.pl", "password", "Anna", "Kowalska");
        User admin = new Admin("admin@wp.pl", "password", "Tomasz", "Kowalski");
        admin.setRole("test");

        ResponseEntity<UserDtoResponse> response = userController.addAdmin(admin);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals("admin", response.getBody().getRole());

        response = userController.addAdmin(employee);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        response = userController.addAdmin(client);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        response = userController.addAdmin(admin);
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void deleteUserTest(){
        User client = new Client("client@wp.pl", "password", "Jan", "Nowak");
        User admin = new Admin("admin@wp.pl", "password", "Tomasz", "Kowalski");
        entityManager.persist(client);
        entityManager.persist(admin);

        ResponseEntity<UserDtoResponse> response = userController.deleteUser(client.getId());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        response = userController.deleteUser(admin.getId());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        response = userController.deleteUser(-1L);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteAdminTest(){
        User client = new Client("client@wp.pl", "password", "Jan", "Nowak");
        User admin = new Admin("admin@wp.pl", "password", "Tomasz", "Kowalski");
        entityManager.persist(client);
        entityManager.persist(admin);

        ResponseEntity<UserDtoResponse> response = userController.deleteAdmin(admin.getId());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        response = userController.deleteAdmin(client.getId());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        response = userController.deleteAdmin(-1L);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void updateUserTest(){
        User client = new Client("client@wp.pl", "password", "Jan", "Nowak");
        User admin = new Admin("admin@wp.pl", "password", "Tomasz", "Kowalski");
        entityManager.persist(client);
        entityManager.persist(admin);

        client.setFirstName("Jakub");
        ResponseEntity<UserDtoResponse> response = userController.updateUser(client.getId(), client);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        response = userController.updateUser(admin.getId(), admin);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        response = userController.updateUser(-1L, client);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void updateAdminTest(){
        User client = new Client("client@wp.pl", "password", "Jan", "Nowak");
        User admin = new Admin("admin@wp.pl", "password", "Tomasz", "Kowalski");
        entityManager.persist(client);
        entityManager.persist(admin);

        admin.setFirstName("Jakub");
        ResponseEntity<UserDtoResponse> response = userController.updateAdmin(admin.getId(), admin);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        response = userController.updateAdmin(client.getId(), client);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        response = userController.updateAdmin(-1L, admin);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void changeNameTest(){
        User client = new Client("client@wp.pl", "password", "Jan", "Nowak");
        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "client@wp.pl";
            }
        };
        ChangeNameRequest request = new ChangeNameRequest();
        request.setFirstName("Jakub");
        request.setLastName("Kowalski");
        request.setEmail("client@wp.pl");

        ResponseEntity<?> response = userController.changeName(request, principal);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        entityManager.persist(client);
        response = userController.changeName(request, principal);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Principal principal2 = new Principal() { @Override
        public String getName() {
            return "testt@wp.pl";
        }
        };

        response = userController.changeName(request, principal2);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void getNameTest(){
        User client = new Client("client@wp.pl", "password", "Jan", "Nowak");
        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "client@wp.pl";
            }
        };
        ResponseEntity<NameResponse> response = userController.getName("client@wp.pl", principal);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        entityManager.persist(client);
        response = userController.getName("client@wp.pl", principal);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Principal principal2 = new Principal() { @Override
        public String getName() {
            return "testt@wp.pl";
        }
        };
        response = userController.getName("client@wp.pl", principal2);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
