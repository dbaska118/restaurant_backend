package org.example.controller.reservation;

import jakarta.persistence.EntityManager;
import org.example.dto.reservation.OpeningHoursRequest;
import org.example.exception.InvalidOpeningHoursException;
import org.example.model.reservation.OpeningHours;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OpeningHoursControllerTest {

    @Autowired
    private OpeningHoursController openingHoursController;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void getOpeningHoursTest() {
        OpeningHours openingHours = new OpeningHours("MONDAY", 1, LocalTime.of(10, 0), LocalTime.of(20, 0));
        entityManager.persist(openingHours);

        ResponseEntity<List<OpeningHours>> response = openingHoursController.getOpeningHours();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(1, response.getBody().size());
    }

    @Test
    public void updateOpeningHoursTest() {
        OpeningHours openingHours = new OpeningHours("MONDAY", 1, LocalTime.of(10, 0), LocalTime.of(20, 0));
        entityManager.persist(openingHours);


        OpeningHoursRequest openingHoursRequest = new OpeningHoursRequest(LocalTime.of(10, 0), LocalTime.of(22, 0));
        ResponseEntity<OpeningHours> response = openingHoursController.updateOpeningHours("MONDAY", openingHoursRequest);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        response = openingHoursController.updateOpeningHours("SUNDAY", openingHoursRequest);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        openingHoursRequest.setCloseTime(LocalTime.of(6, 0));
        response = openingHoursController.updateOpeningHours("MONDAY", openingHoursRequest);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
