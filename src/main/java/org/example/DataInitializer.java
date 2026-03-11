package org.example;

import jakarta.annotation.PostConstruct;
import org.example.Model.Dish;
import org.example.Model.DishType;
import org.example.Service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final DishService dishService;

    @Autowired
    public DataInitializer(DishService dishService) {
        this.dishService = dishService;
    }

    @PostConstruct
    public void init() {
        if(dishService.readAllDishes().isEmpty()) {
            Dish dish = new Dish("Sok pomarańczowy", "250 ml", 15, DishType.DRINK);
            Dish dish2 = new Dish("Zupa pomidorowa", "makaron", 20, DishType.SOUP);
            Dish dish3 = new Dish("Pizza", "ser, szynka, pieczarki", 50.50, DishType.MAIN);
            Dish dish4 = new Dish("Lody", "waniliowe/czekoladowe", 12, DishType.DESSERT);

            dishService.createDish(dish);
            dishService.createDish(dish2);
            dishService.createDish(dish3);
            dishService.createDish(dish4);
        }
    }
}
