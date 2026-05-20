package org.example.dto.reservation;

import java.time.LocalDateTime;

public class ReservationRequestDto {

    private String email;

    private long tableId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public ReservationRequestDto() {
    }

    public String getEmail() {
        return email;
    }

    public long getTableId() {
        return tableId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTableId(long tableId) {
        this.tableId = tableId;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }



}
