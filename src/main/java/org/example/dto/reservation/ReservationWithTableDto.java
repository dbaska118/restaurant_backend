package org.example.dto.reservation;

import org.example.dto.restaurantTable.RestaurantTableReservationDto;
import org.example.model.reservation.ReservationStatus;

import java.time.LocalDateTime;

public class ReservationWithTableDto {

    private Long id;

    private String email;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private ReservationStatus reservationStatus;

    private RestaurantTableReservationDto restaurantTableReservationDTO;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public RestaurantTableReservationDto getRestaurantTableReservationDTO() {
        return restaurantTableReservationDTO;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public void setRestaurantTableReservationDTO(RestaurantTableReservationDto restaurantTableReservationDTO) {
        this.restaurantTableReservationDTO = restaurantTableReservationDTO;
    }
}
