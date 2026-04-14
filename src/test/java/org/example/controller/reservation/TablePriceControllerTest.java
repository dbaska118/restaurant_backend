package org.example.controller.reservation;

import jakarta.persistence.EntityManager;
import org.apache.coyote.Response;
import org.example.model.reservation.RestaurantTable;
import org.example.model.reservation.TablePrice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TablePriceControllerTest {

    @Autowired
    private TablePriceController tablePriceController;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void getTest(){
        TablePrice tablePrice = new TablePrice(4, 50);
        entityManager.persist(tablePrice);

        ResponseEntity<List<TablePrice>> response = tablePriceController.getAllTablePrice();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void createTest(){
        TablePrice tablePrice = new TablePrice(4, 50);
        TablePrice tablePrice2 = new TablePrice(4, 70);

        ResponseEntity<TablePrice> response = tablePriceController.createTablePrice(tablePrice);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        response = tablePriceController.createTablePrice(tablePrice2);
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void updateTest(){
        TablePrice tablePrice = new TablePrice(4, 50);
        entityManager.persist(tablePrice);

        tablePrice.setPrice(70);
        ResponseEntity<TablePrice> response = tablePriceController.updateTablePrice(4, tablePrice);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        response = tablePriceController.updateTablePrice(1, tablePrice);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteTest(){
        TablePrice tablePrice = new TablePrice(4, 50);
        TablePrice tablePrice2 = new TablePrice(8, 80);
        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 8);

        entityManager.persist(tablePrice);
        entityManager.persist(tablePrice2);
        entityManager.persist(restaurantTable);

        ResponseEntity<TablePrice> response = tablePriceController.deleteTablePrice(4);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        response = tablePriceController.deleteTablePrice(8);
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        response = tablePriceController.deleteTablePrice(1);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());


    }

    @Test
    public void getPossibleNumberOfChairs(){
        TablePrice tablePrice = new TablePrice(7, 30);
        TablePrice tablePrice2 = new TablePrice(4, 50);
        entityManager.persist(tablePrice);
        entityManager.persist(tablePrice2);

        ResponseEntity<List<Integer>> response = tablePriceController.getPossibleNumberOfChairs();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(2, response.getBody().size());
    }
}
