package org.example.dto.reservation;

import org.example.model.reservation.Reservation;
import org.example.model.restaurantTable.RestaurantTable;
import org.example.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationPublicDto toReservationPublicDTO(Reservation reservation) {
        ReservationPublicDto reservationPublicDTO = new ReservationPublicDto();
        reservationPublicDTO.setTableId(reservation.getRestaurantTable().getId());
        reservationPublicDTO.setStartTime(reservation.getStartTime());
        reservationPublicDTO.setEndTime(reservation.getEndTime());
        return reservationPublicDTO;
    }

    public ReservationResponseDto toReservationResponseDto(Reservation reservation) {
        ReservationResponseDto reservationResponseDto = new ReservationResponseDto();
        reservationResponseDto.setId(reservation.getId());
        reservationResponseDto.setEmail(reservation.getEmail());
        reservationResponseDto.setTableName(reservation.getRestaurantTable().getName());
        reservationResponseDto.setStartTime(reservation.getStartTime());
        reservationResponseDto.setEndTime(reservation.getEndTime());
        reservationResponseDto.setPrice(reservation.getPrice());
        reservationResponseDto.setReservationCode(reservation.getReservationCode());
        reservationResponseDto.setReservationStatus(reservation.getReservationStatus());
        reservationResponseDto.setNumberOfChairs(reservation.getRestaurantTable().getNumberOfChairs());
        return reservationResponseDto;
    }

    public Reservation toReservation(ReservationRequestDto reservationRequestDto, User user, RestaurantTable restaurantTable) {
        Reservation reservation = new Reservation();
        reservation.setEmail(reservationRequestDto.getEmail());
        reservation.setUser(user);
        reservation.setRestaurantTable(restaurantTable);
        reservation.setStartTime(reservationRequestDto.getStartTime());
        reservation.setEndTime(reservationRequestDto.getEndTime());

        return reservation;
    }

    public NextReservationDTO toNextReservationDTO(Reservation reservation) {
        NextReservationDTO nextReservationDTO = new NextReservationDTO();
        nextReservationDTO.setId(reservation.getId());
        nextReservationDTO.setTableId(reservation.getRestaurantTable().getId());
        nextReservationDTO.setStartTime(reservation.getStartTime());
        nextReservationDTO.setEndTime(reservation.getEndTime());

        return nextReservationDTO;
    }
}
