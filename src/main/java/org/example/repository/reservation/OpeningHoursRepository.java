package org.example.repository.reservation;

import org.example.model.reservation.OpeningHours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpeningHoursRepository extends JpaRepository<OpeningHours, String> {

    public List<OpeningHours> findAllByOrderByDayOrderAsc();
}
