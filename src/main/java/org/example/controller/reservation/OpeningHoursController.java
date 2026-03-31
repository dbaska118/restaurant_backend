package org.example.controller.reservation;

import org.example.dto.reservation.OpeningHoursRequest;
import org.example.exception.DayOfWeekNotFoundException;
import org.example.exception.InvalidOpeningHoursException;
import org.example.model.reservation.OpeningHours;
import org.example.service.reservation.OpeningHoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/openingHours")
public class OpeningHoursController {

    private final OpeningHoursService openingHoursService;

    @Autowired
    public OpeningHoursController(OpeningHoursService openingHoursService) {
        this.openingHoursService = openingHoursService;
    }


    @GetMapping
    public ResponseEntity<List<OpeningHours>> getOpeningHours(){
        List<OpeningHours> openingHours = openingHoursService.readAllOpeningHours();
        return new ResponseEntity<>(openingHours, HttpStatus.OK);
    }

    @PutMapping("/{dayOfWeek}")
    public ResponseEntity<OpeningHours> updateOpeningHours(@PathVariable String dayOfWeek, @RequestBody OpeningHoursRequest openingHoursRequest){
        try {
            OpeningHours openingHours = openingHoursService.updateOpeningHours(dayOfWeek, openingHoursRequest);
            return new ResponseEntity<>(openingHours, HttpStatus.OK);
        }
        catch(InvalidOpeningHoursException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch(DayOfWeekNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
