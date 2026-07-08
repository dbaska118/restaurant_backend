package org.example.dto.reservation;

import org.example.model.reservation.ReservationStatus;

import java.time.LocalDateTime;

public class NextReservationDTO {

    private Long id;

    private Long TableId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private ReservationStatus status;

    public Long getId() {
        return id;
    }

    public Long getTableId() {
        return TableId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTableId(Long tableId) {
        TableId = tableId;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
