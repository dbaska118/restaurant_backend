package org.example.repository.reservation;

import org.example.model.reservation.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    public boolean existsByNumberOfChairsAndActiveTrue(int numberOfChairs);
}
