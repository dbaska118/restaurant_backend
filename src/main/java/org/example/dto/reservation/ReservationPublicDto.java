package org.example.dto.reservation;

import java.time.LocalDateTime;

public class ReservationPublicDto {

    private long tableId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;


    public ReservationPublicDto() {
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
