package org.example.model.reservation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TablePriceTest {

    @Test
    public void getTest(){
        TablePrice tablePrice = new TablePrice(4, 80);
        Assertions.assertEquals(4, tablePrice.getNumberOfChairs());
        Assertions.assertEquals(80, tablePrice.getPrice());
    }

    @Test
    public void setTest(){
        TablePrice tablePrice = new TablePrice();
        tablePrice.setNumberOfChairs(2);
        tablePrice.setPrice(50);

        Assertions.assertEquals(2, tablePrice.getNumberOfChairs());
        Assertions.assertEquals(50, tablePrice.getPrice());
    }

}
