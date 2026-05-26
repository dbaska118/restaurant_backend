package org.example.repository.reservation;

import org.example.model.reservation.Reservation;
import org.example.model.reservation.ReservationStatus;
import org.example.model.reservation.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByEmailAndReservationStatus(String email, ReservationStatus reservationStatus);

    List<Reservation> findByRestaurantTableAndStartTimeBeforeAndEndTimeAfter(RestaurantTable restaurantTable, LocalDateTime end, LocalDateTime start);
}
