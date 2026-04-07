package org.example.repository.reservation;

import jakarta.persistence.LockModeType;
import org.example.model.reservation.TablePrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface TablePriceRepository extends JpaRepository<TablePrice, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Optional<TablePrice> findByNumberOfChairs(int numberOfChairs);

    public List<TablePrice> findAllByOrderByNumberOfChairsAsc();
}
