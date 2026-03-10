package Model;

import org.example.Model.Dish;
import org.example.Model.DishType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DishTest {

    @Test
    void getTest() {
        Dish dish = new Dish("Pizza", "ser, szynka, pieczarki", 50.50, DishType.MAIN);

        Assertions.assertNull(dish.getId());
        Assertions.assertEquals("Pizza", dish.getName());
        Assertions.assertEquals("ser, szynka, pieczarki", dish.getDescription());
        Assertions.assertEquals(50.50, dish.getPrice());
        Assertions.assertEquals(dish.getDishType(), DishType.MAIN);
    }

    @Test
    public void setTest() {
        Dish dish = new Dish();
        dish.setName("Sok pomarańczowy");
        dish.setDescription("250 ml");
        dish.setPrice(15);
        dish.setDishType(DishType.DRINK);

        Assertions.assertEquals("Sok pomarańczowy", dish.getName());
        Assertions.assertEquals("250 ml", dish.getDescription());
        Assertions.assertEquals(15, dish.getPrice());
        Assertions.assertEquals(DishType.DRINK, dish.getDishType());
    }


}
