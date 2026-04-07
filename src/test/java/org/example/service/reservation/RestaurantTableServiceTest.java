package org.example.service.reservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import org.example.exception.RestaurantTableNotFoundException;
import org.example.exception.TablePriceNotFoundException;
import org.example.model.reservation.RestaurantTable;
import org.example.model.reservation.TablePrice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RestaurantTableServiceTest {

    @Autowired
    private RestaurantTableService restaurantTableService;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void createTest(){
        TablePrice tablePrice = new TablePrice(2, 89);
        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 2);
        RestaurantTable restaurantTable2 = new RestaurantTable("Stolik 2", 3);
        entityManager.persist(tablePrice);

        List<RestaurantTable> restaurantTableList = entityManager.createQuery("from RestaurantTable", RestaurantTable.class).getResultList();
        Assertions.assertEquals(0, restaurantTableList.size());

        restaurantTableService.createRestaurantTable(restaurantTable);
        restaurantTableList = entityManager.createQuery("from RestaurantTable", RestaurantTable.class).getResultList();
        Assertions.assertEquals(1, restaurantTableList.size());
        Assertions.assertEquals(restaurantTable, restaurantTableList.get(0));

        Assertions.assertThrows(TablePriceNotFoundException.class, () -> {
            restaurantTableService.createRestaurantTable(restaurantTable2);
        });
    }

    @Test
    public void getAllTest(){
        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 5);
        RestaurantTable restaurantTable2 = new RestaurantTable("Stolik 2", 3);
        entityManager.persist(restaurantTable);
        entityManager.persist(restaurantTable2);

        List<RestaurantTable> restaurantTableList = restaurantTableService.getAllRestaurantTables();
        Assertions.assertEquals(2, restaurantTableList.size());
        Assertions.assertEquals(restaurantTable2, restaurantTableList.get(0));
        Assertions.assertEquals(restaurantTable, restaurantTableList.get(1));
    }

    @Test
    public void deleteTest(){
        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 5);
        entityManager.persist(restaurantTable);

        List<RestaurantTable> list = restaurantTableService.getAllRestaurantTables();
        Assertions.assertEquals(1, list.size());

        RestaurantTable restaurantTableDB = restaurantTableService.deleteRestaurantTable(restaurantTable.getId());
        Assertions.assertFalse(restaurantTableDB.getActive());

        Assertions.assertThrows(RestaurantTableNotFoundException.class, () -> {
            restaurantTableService.deleteRestaurantTable(-1L);
        });
    }

    @Test
    public void updateTest(){
        TablePrice tablePrice = new TablePrice(2, 89);
        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 3);
        entityManager.persist(tablePrice);
        entityManager.persist(restaurantTable);

        restaurantTable.setNumberOfChairs(4);
        Assertions.assertThrows(TablePriceNotFoundException.class, () -> {
            restaurantTableService.updateRestaurantTable(restaurantTable.getId(), restaurantTable);
        });

        restaurantTable.setNumberOfChairs(2);
        restaurantTable.setName("Stolik 2");
        RestaurantTable restaurantTableDB = restaurantTableService.updateRestaurantTable(restaurantTable.getId(), restaurantTable);
        Assertions.assertEquals("Stolik 2", restaurantTableDB.getName());
        Assertions.assertEquals(2, restaurantTableDB.getNumberOfChairs());

        Assertions.assertThrows(RestaurantTableNotFoundException.class, () -> {
            restaurantTableService.updateRestaurantTable(-1L, restaurantTable);
        });
    }
}
