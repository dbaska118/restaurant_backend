package org.example.service.reservation;

import org.example.dto.reservation.OpeningHoursRequest;
import org.example.exception.DayOfWeekNotFoundException;
import org.example.exception.InvalidOpeningHoursException;
import org.example.model.reservation.OpeningHours;
import org.example.service.dish.DishService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@DataJpaTest
@Import(OpeningHoursService.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class OpeningHoursServiceTest {

    @Autowired
    private OpeningHoursService openingHoursService;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void getAllTest(){
        LocalTime startTime = LocalTime.of(8,0);
        LocalTime endTime = LocalTime.of(20, 0);

        OpeningHours monday = new OpeningHours("MONDAY", 1, startTime, endTime);
        OpeningHours tuesday = new OpeningHours("TUESDAY", 2, startTime, endTime);

        entityManager.persist(monday);
        entityManager.persist(tuesday);

        List<OpeningHours> openingHoursList = openingHoursService.getAllOpeningHours();
        Assertions.assertEquals(2, openingHoursList.size());
        Assertions.assertEquals(monday, openingHoursList.get(0));
        Assertions.assertEquals(tuesday, openingHoursList.get(1));
    }

    @Test
    public void updateTest(){
        LocalTime startTime = LocalTime.of(8,0);
        LocalTime endTime = LocalTime.of(20, 0);
        OpeningHours monday = new OpeningHours("MONDAY", 1, startTime, endTime);

        entityManager.persist(monday);


        LocalTime newStartTime = LocalTime.of(10, 0);
        LocalTime newEndTime = LocalTime.of(22, 0);
        OpeningHoursRequest request = new OpeningHoursRequest(newStartTime, newEndTime);


        OpeningHours openingHoursDB = openingHoursService.updateOpeningHours("MONDAY",request);
        Assertions.assertEquals("MONDAY", openingHoursDB.getDayOfWeek());
        Assertions.assertEquals(1, openingHoursDB.getDayOrder());
        Assertions.assertEquals(newStartTime, openingHoursDB.getOpenTime());
        Assertions.assertEquals(newEndTime, openingHoursDB.getCloseTime());

        Assertions.assertThrows(DayOfWeekNotFoundException.class, () -> {
           openingHoursService.updateOpeningHours("FRIDAY", request);
        });

        request.setCloseTime(LocalTime.of(6, 0));
        Assertions.assertThrows(InvalidOpeningHoursException.class, () -> {
            openingHoursService.updateOpeningHours("MONDAY", request);
        });

    }


}
