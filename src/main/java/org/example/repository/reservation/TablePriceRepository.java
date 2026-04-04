package org.example.repository.reservation;

import org.example.model.reservation.TablePrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TablePriceRepository extends JpaRepository<TablePrice, Long> {
}
