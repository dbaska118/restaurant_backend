package org.example.service.dish;

import org.example.model.dish.Dish;
import org.example.model.dish.DishType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@Import(DishService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DishServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DishService dishService;

    @Test
    public void createTest(){
        Dish dish = new Dish("Sok pomarańczowy", "250 ml", 15, DishType.DRINK);
        List<Dish> dishList = entityManager.getEntityManager().createQuery("select d from Dish d", Dish.class).getResultList();
        Assertions.assertTrue(dishList.isEmpty());

        dishService.createDish(dish);
        dishList = entityManager.getEntityManager().createQuery("select d from Dish d", Dish.class).getResultList();
        Assertions.assertEquals(1, dishList.size());
        Assertions.assertEquals(dish, dishList.get(0));
    }

    @Test
    public void deleteTest()  {
        Dish dish = new Dish("Sok pomarańczowy", "250 ml", 15, DishType.DRINK);
        entityManager.getEntityManager().persist(dish);

        List<Dish> dishList = entityManager.getEntityManager().createQuery("select d from Dish d", Dish.class).getResultList();
        Assertions.assertEquals(1, dishList.size());

        dishService.deleteDish(dish.getId());
        dishList = entityManager.getEntityManager().createQuery("select d from Dish d", Dish.class).getResultList();
        Assertions.assertTrue(dishList.isEmpty());

        Assertions.assertThrows(RuntimeException.class, () -> dishService.deleteDish(null));
    }

    @Test
    public void updateTest() {
        Dish dish = new Dish("Sok pomarańczowy", "250 ml", 15, DishType.DRINK);
        entityManager.getEntityManager().persist(dish);

        dish.setName("Sok jabłkowy");
        dishService.updateDish(dish.getId(), dish);

        Dish dishDB = entityManager.getEntityManager().find(Dish.class, dish.getId());
        Assertions.assertEquals("Sok jabłkowy", dishDB.getName());

        dish.setDescription("500 ml");
        Assertions.assertThrows(RuntimeException.class, () -> dishService.updateDish(null, dish));
    }

    @Test
    public void readAllTest(){
        Dish dish = new Dish("Sok pomarańczowy", "250 ml", 15, DishType.DRINK);
        Dish dish2 = new Dish("Pizza", "ser, szynka, pieczarki", 50, DishType.MAIN);
        entityManager.getEntityManager().persist(dish);
        entityManager.getEntityManager().persist(dish2);

        List<Dish> dishList = dishService.readAllDishes();
        Assertions.assertEquals(2, dishList.size());
        Assertions.assertEquals(dish, dishList.get(0));
        Assertions.assertEquals(dish2, dishList.get(1));
    }


}
