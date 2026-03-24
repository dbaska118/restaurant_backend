package org.example.service.dish;

import org.example.exception.DishNotFoundException;
import org.example.model.dish.Dish;
import org.example.repository.dish.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .orElseThrow(DishNotFoundException::new);
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
              .orElseThrow(DishNotFoundException::new);
    }

    public List<Dish> readAllDishes() {
        return dishRepository.findAllByOrderByIdAsc();
    }
    
}
