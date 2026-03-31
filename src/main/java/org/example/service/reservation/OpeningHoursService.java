package org.example.service.reservation;

import org.example.dto.reservation.OpeningHoursRequest;
import org.example.exception.DayOfWeekNotFoundException;
import org.example.exception.InvalidOpeningHoursException;
import org.example.model.reservation.OpeningHours;
import org.example.repository.reservation.OpeningHoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpeningHoursService {

    private final OpeningHoursRepository openingHoursRepository;

    @Autowired
    public OpeningHoursService(OpeningHoursRepository openingHoursRepository) {
        this.openingHoursRepository = openingHoursRepository;
    }

    public List<OpeningHours> readAllOpeningHours() {
        return openingHoursRepository.findAllByOrderByDayOrderAsc();
    }

    public OpeningHours updateOpeningHours(String dayOfWeek, OpeningHoursRequest openingHours) {
        if (openingHours.getOpenTime().isAfter(openingHours.getCloseTime())) {
            throw new InvalidOpeningHoursException();
        }

        OpeningHours openingHoursDB = openingHoursRepository.findById(dayOfWeek).orElseThrow(DayOfWeekNotFoundException::new);
        openingHoursDB.setOpenTime(openingHours.getOpenTime());
        openingHoursDB.setCloseTime(openingHours.getCloseTime());
        return openingHoursRepository.save(openingHoursDB);
    }
}
