package org.example.Service;

import org.example.Model.Dish;
import org.example.Model.DishType;
import org.example.Repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishService {

    private final DishRepository dishRepository;

    @Autowired
    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public Dish createDish(Dish dish) {
        return dishRepository.save(dish);
    }

    public Dish deleteDish(Long id)  {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException());
       dishRepository.deleteById(id);
       return dish;
    }

    public Dish updateDish(Long id, Dish dish)  {
      return dishRepository.findById(id)
              .map(dishDB -> {
                  dishDB.setName(dish.getName());
                  dishDB.setDescription(dish.getDescription());
                  dishDB.setPrice(dish.getPrice());
                  dishDB.setDishType(dish.getDishType());
                  return dishRepository.save(dishDB);
              })
              .orElseThrow(() -> new RuntimeException());
    }

    public List<Dish> readAllDishes() {
        return dishRepository.findAll();
    }
    
}
