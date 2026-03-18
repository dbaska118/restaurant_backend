package org.example.controller.dish;

import org.example.model.dish.Dish;
import org.example.service.dish.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dish")
public class DishController {

    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping
    public ResponseEntity<List<Dish>> getAllDishes() {
        List<Dish> dishList = dishService.readAllDishes();
        return new ResponseEntity<>(dishList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Dish> createDish(@RequestBody Dish dish) {
        Dish dishDB = dishService.createDish(dish);
        return new ResponseEntity<>(dishDB, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Dish> deleteDish(@PathVariable long id) {
        try {
            Dish dishDB = dishService.deleteDish(id);
            return new ResponseEntity<>(dishDB, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dish> updateDish(@PathVariable long id, @RequestBody Dish dish) {
        try {
            Dish dishDB = dishService.updateDish(id, dish);
            return new ResponseEntity<>(dishDB, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
