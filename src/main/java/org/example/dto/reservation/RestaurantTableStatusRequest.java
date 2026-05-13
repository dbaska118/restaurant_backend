package org.example.dto.reservation;

import org.example.model.reservation.RestaurantTableStatus;

public class RestaurantTableStatusRequest {

    private Long id;

    private Integer version;

    private RestaurantTableStatus status;

    public RestaurantTableStatusRequest() {
    }

    public RestaurantTableStatusRequest(Long id, Integer version, RestaurantTableStatus status) {
        this.id = id;
        this.version = version;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public RestaurantTableStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setStatus(RestaurantTableStatus status) {
        this.status = status;
    }
}
