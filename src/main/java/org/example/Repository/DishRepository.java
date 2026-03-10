package org.example.Repository;

import org.example.Model.Dish;
import org.example.Model.DishType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> getDishesByDishType(DishType dishType);
}
