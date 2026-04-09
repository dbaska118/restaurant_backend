package org.example.controller.reservation;

import jakarta.persistence.EntityManager;
import org.example.dto.reservation.AddBalanceRequest;
import org.example.dto.reservation.BalanceOperationDTO;
import org.example.model.reservation.BalanceOperation;
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

import java.security.Principal;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BalanceOperationControllerTest {

    @Autowired
    private BalanceOperationController balanceOperationController;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void addFundsTest(){
        User client = new Client("client@wp.pl", "password", "Jan", "Nowak");
        User admin = new Admin("admin@wp.pl", "password", "Tomasz", "Kowal");
        entityManager.persist(client);
        entityManager.persist(admin);
        AddBalanceRequest addBalanceRequest = new AddBalanceRequest("user@wp.pl", 100);
        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "client@wp.pl";
            }
        };

        ResponseEntity<BalanceOperationDTO> response = balanceOperationController.addFunds(addBalanceRequest, principal);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());


        addBalanceRequest = new AddBalanceRequest("client@wp.pl", 200);
        response = balanceOperationController.addFunds(addBalanceRequest, principal);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());


        principal = new Principal() {
            @Override
            public String getName() {
                return "client2@wp.pl";
            }
        };
        addBalanceRequest = new AddBalanceRequest("client2@wp.pl", 200);
        response = balanceOperationController.addFunds(addBalanceRequest, principal);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());


        principal = new Principal() {
            @Override
            public String getName() {
                return "admin@wp.pl";
            }
        };
        addBalanceRequest = new AddBalanceRequest("admin@wp.pl", 200);
        response = balanceOperationController.addFunds(addBalanceRequest, principal);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void getBalanceOperationsTest(){
        User client = new Client("client@wp.pl", "password", "Jan", "Nowak");
        User admin = new Admin("admin@wp.pl", "password", "Tomasz", "Kowal");
        entityManager.persist(client);
        entityManager.persist(admin);
        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "client@wp.pl";
            }
        };

        ResponseEntity<List<BalanceOperationDTO>> response = balanceOperationController.getAllBalanceOperations("user@wp.pl", principal);
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());


        response = balanceOperationController.getAllBalanceOperations("client@wp.pl", principal);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());


        principal = new Principal() {
            @Override
            public String getName() {
                return "client2@wp.pl";
            }
        };
        response = balanceOperationController.getAllBalanceOperations("client2@wp.pl", principal);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());


        principal = new Principal() {
            @Override
            public String getName() {
                return "admin@wp.pl";
            }
        };
        response = balanceOperationController.getAllBalanceOperations("admin@wp.pl", principal);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    public void addFundsEmployeeTest(){
        User client = new Client("client@wp.pl", "password", "Jan", "Nowak");
        User admin = new Admin("admin@wp.pl", "password", "Tomasz", "Kowal");
        entityManager.persist(client);
        entityManager.persist(admin);
        AddBalanceRequest addBalanceRequest = new AddBalanceRequest("user@wp.pl", 100);


        addBalanceRequest = new AddBalanceRequest("client@wp.pl", 200);
        ResponseEntity<BalanceOperationDTO> response = balanceOperationController.addFundsEmployee(addBalanceRequest);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());



        addBalanceRequest = new AddBalanceRequest("client2@wp.pl", 200);
        response = balanceOperationController.addFundsEmployee(addBalanceRequest);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());


        addBalanceRequest = new AddBalanceRequest("admin@wp.pl", 200);
        response = balanceOperationController.addFundsEmployee(addBalanceRequest);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
