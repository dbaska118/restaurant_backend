package org.example.repository.reservation;

import jakarta.persistence.LockModeType;
import org.example.model.reservation.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    public boolean existsByNumberOfChairsAndActiveTrue(int numberOfChairs);

    public List<RestaurantTable> findAllByActiveTrueOrderByNumberOfChairsAsc();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Optional<RestaurantTable> findWithLockById(long id);
}
