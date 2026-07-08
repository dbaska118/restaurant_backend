package org.example.repository.reservation;

import org.example.model.reservation.Reservation;
import org.example.model.reservation.ReservationStatus;
import org.example.model.restaurantTable.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByEmailAndReservationStatus(String email, ReservationStatus reservationStatus);

    List<Reservation> findByRestaurantTableAndStartTimeBeforeAndEndTimeAfterAndReservationStatusNot(RestaurantTable restaurantTable, LocalDateTime end, LocalDateTime start, ReservationStatus reservationStatus);


    @Query(value = "SELECT DISTINCT ON (r.restaurant_table_id) r.* FROM reservation r " +
            "JOIN restaurant_table t ON r.restaurant_table_id = t.id " +
            "WHERE r.start_time >= :bufferTime AND r.start_time <= :endOfDay " +
            "AND r.reservation_status IN (:statuses) " +
            "AND t.active = true " +
            "ORDER BY r.restaurant_table_id ASC, r.start_time ASC",
            nativeQuery = true)
    List<Reservation> findNextReservations(@Param("bufferTime") LocalDateTime bufferTime, @Param("endOfDay") LocalDateTime endOfDay, @Param("statuses") List<ReservationStatus> statuses);

    List<Reservation> findByEmailAndReservationStatusAndStartTimeAfterAndStartTimeBefore(String email, ReservationStatus reservationStatus, LocalDateTime start, LocalDateTime end);

    Optional<Reservation> findByRestaurantTableIdAndReservationStatus(Long restaurantTableId, ReservationStatus reservationStatus);

}


