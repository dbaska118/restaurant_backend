package org.example.repository.reservation;

import org.example.model.reservation.Reservation;
import org.example.model.reservation.ReservationStatus;
import org.example.model.reservation.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByEmailAndReservationStatus(String email, ReservationStatus reservationStatus);

    List<Reservation> findByRestaurantTableAndStartTimeBeforeAndEndTimeAfter(RestaurantTable restaurantTable, LocalDateTime end, LocalDateTime start);


    @Query(value = "SELECT DISTINCT ON (restaurant_table_id) * FROM reservation " +
            "WHERE start_time >= :bufferTime AND start_time <= :endOfDay " +
            "AND reservation_status = :status " +
            "ORDER BY restaurant_table_id, start_time ASC",
            nativeQuery = true)
    List<Reservation> findNextReservations(@Param("bufferTime") LocalDateTime bufferTime, @Param("endOfDay") LocalDateTime endOfDay, @Param("status") ReservationStatus status);

}
