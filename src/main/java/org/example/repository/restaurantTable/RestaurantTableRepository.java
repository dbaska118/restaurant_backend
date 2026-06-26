package org.example.repository.restaurantTable;

import jakarta.persistence.LockModeType;
import org.example.model.restaurantTable.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    public boolean existsByNumberOfChairsAndActiveTrue(int numberOfChairs);

    public List<RestaurantTable> findAllByActiveTrueOrderByNumberOfChairsAsc();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Optional<RestaurantTable> findWithLockById(long id);

    @Query("SELECT t from RestaurantTable t " +
            "where t.active = true " +
            "and t.numberOfChairs >= ?1 " +
            "and t.id NOT in " +
            "(select r.restaurantTable.id from Reservation r " +
            "where r.reservationStatus = 0 " +
            "and r.startTime < ?3 " +
            "and r.endTime > ?2" +
            ")" +
            "order by t.numberOfChairs ASC ")
    List<RestaurantTable> findAllFreeTables(int minChairs, LocalDateTime start, LocalDateTime end);
}

