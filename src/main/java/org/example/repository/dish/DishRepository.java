package org.example.repository.dish;

import org.example.model.dish.Dish;
import org.example.model.dish.DishType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> getDishesByDishType(DishType dishType);
}
