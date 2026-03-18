package org.example.controller.dish;

import jakarta.persistence.EntityManager;
import org.example.model.dish.Dish;
import org.example.model.dish.DishType;
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
public class DishControllerTest {

    @Autowired
    private DishController dishController;

    @Autowired
    private EntityManager entityManager;


    @Test
    public void getTest(){
        Dish dish = new Dish("Pizza", "ser, szynka, pieczarki", 50.50, DishType.MAIN);
        Dish dish2 = new Dish("Sok pomarańczowy", "250 ml", 15, DishType.DRINK);
        entityManager.persist(dish);
        entityManager.persist(dish2);

        ResponseEntity<List<Dish>> response = dishController.getAllDishes();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(2, response.getBody().size());
    }

    @Test
    public void postTest(){
        Dish dish = new Dish("Pizza", "ser, szynka, pieczarki", 50.50, DishType.MAIN);

        ResponseEntity<Dish> response = dishController.createDish(dish);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void putTest(){
        Dish dish = new Dish("Pizza", "ser, szynka, pieczarki", 50.50, DishType.MAIN);
        entityManager.persist(dish);

        dish.setName("Sok");
        ResponseEntity<Dish> response = dishController.updateDish(dish.getId(), dish);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        response = dishController.updateDish(-1L, dish);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteTest(){
        Dish dish = new Dish("Pizza", "ser, szynka, pieczarki", 50.50, DishType.MAIN);
        entityManager.persist(dish);

        ResponseEntity<Dish> response = dishController.deleteDish(dish.getId());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        response = dishController.deleteDish(-1L);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
