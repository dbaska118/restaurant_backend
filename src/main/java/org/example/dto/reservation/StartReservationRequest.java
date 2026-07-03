package org.example.dto.reservation;

public class StartReservationRequest {

    Long reservationId;
    Long tableId;
    String reservationCode;
    Integer version;

    public Long getReservationId() {
        return reservationId;
    }

    public Long getTableId() {
        return tableId;
    }

    public String getReservationCode() {
        return reservationCode;
    }

    public Integer getVersion() {
        return version;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
