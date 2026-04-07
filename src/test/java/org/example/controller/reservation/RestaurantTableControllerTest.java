package org.example.controller.reservation;

import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.EntityManager;
import org.checkerframework.checker.units.qual.A;
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
public class RestaurantTableControllerTest {

    @Autowired
    private RestaurantTableController restaurantTableController;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void getAllTest(){
        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 5);
        entityManager.persist(restaurantTable);

        ResponseEntity<List<RestaurantTable>> response = restaurantTableController.getAllRestaurantTables();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void createTest(){
        TablePrice tablePrice = new TablePrice(5, 120);
        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 5);
        RestaurantTable restaurantTable2 = new RestaurantTable("Stolik 2", 10);

        entityManager.persist(tablePrice);
        ResponseEntity<RestaurantTable> response = restaurantTableController.createRestaurantTable(restaurantTable);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        response = restaurantTableController.createRestaurantTable(restaurantTable2);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void updateTest(){
        TablePrice tablePrice = new TablePrice(5, 120);
        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 7);
        entityManager.persist(restaurantTable);
        entityManager.persist(tablePrice);

        restaurantTable.setNumberOfChairs(10);
        ResponseEntity<RestaurantTable> response = restaurantTableController.updateRestaurantTable(restaurantTable.getId(), restaurantTable);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        restaurantTable.setNumberOfChairs(5);
        response = restaurantTableController.updateRestaurantTable(restaurantTable.getId(), restaurantTable);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        response = restaurantTableController.updateRestaurantTable(-1L, restaurantTable);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteTest(){
        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 7);
        entityManager.persist(restaurantTable);

        ResponseEntity<RestaurantTable> response = restaurantTableController.deleteRestaurantTable(restaurantTable.getId());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        response = restaurantTableController.deleteRestaurantTable(-1L);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
