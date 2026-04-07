package org.example.service.reservation;


import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import org.example.exception.TablePriceExistException;
import org.example.exception.TablePriceNotFoundException;
import org.example.exception.TablesExistException;
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
public class TablePriceServiceTest {

    @Autowired
    private TablePriceService tablePriceService;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void createTest(){
        TablePrice tablePrice = new TablePrice(4, 50);
        TablePrice tablePrice1 = new TablePrice(4, 100);

        List<TablePrice> tablePriceList = entityManager.createQuery("from TablePrice").getResultList();
        Assertions.assertTrue(tablePriceList.isEmpty());

        tablePriceService.createTablePrice(tablePrice);
        tablePriceList = entityManager.createQuery("from TablePrice").getResultList();
        Assertions.assertEquals(1, tablePriceList.size());
        Assertions.assertEquals(tablePrice, tablePriceList.get(0));

        Assertions.assertThrows(TablePriceExistException.class, () -> tablePriceService.createTablePrice(tablePrice1));
    }

    @Test
    public void getAllTest(){
        TablePrice tablePrice = new TablePrice(4, 50);
        TablePrice tablePrice1 = new TablePrice(10, 100);

        entityManager.persist(tablePrice);
        entityManager.persist(tablePrice1);

        List<TablePrice> tablePriceList = tablePriceService.getAllTablePrices();
        Assertions.assertEquals(2, tablePriceList.size());
        Assertions.assertEquals(tablePrice, tablePriceList.get(0));
        Assertions.assertEquals(tablePrice1, tablePriceList.get(1));
    }

    @Test
    public void deleteTest(){
        TablePrice tablePrice = new TablePrice(2, 30);
        TablePrice tablePrice1 = new TablePrice(4, 50);
        RestaurantTable restaurantTable = new RestaurantTable("Stolik 1", 4);
        entityManager.persist(tablePrice);
        entityManager.persist(tablePrice1);
        entityManager.persist(restaurantTable);

        List<TablePrice> tablePriceList = entityManager.createQuery("from TablePrice").getResultList();
        Assertions.assertEquals(2, tablePriceList.size());

        tablePriceService.deleteTablePrice(2);
        tablePriceList = entityManager.createQuery("from TablePrice").getResultList();
        Assertions.assertEquals(1, tablePriceList.size());

        Assertions.assertThrows(TablesExistException.class, () -> tablePriceService.deleteTablePrice(4));
    }

    @Test
    public void updateTest(){
        TablePrice tablePrice = new TablePrice(2, 30);
        entityManager.persist(tablePrice);

        tablePrice.setPrice(59.99);
        tablePriceService.updateTablePrice(2, tablePrice);
        TablePrice tablePriceDB = entityManager.find(TablePrice.class, tablePrice.getId());
        Assertions.assertEquals(59.99, tablePriceDB.getPrice());

        tablePrice.setPrice(100);
        Assertions.assertThrows(TablePriceNotFoundException.class, () -> tablePriceService.updateTablePrice(-1, tablePrice));

    }
}
