package org.example.controller.reservation;

import org.example.dto.reservation.*;
import org.example.exception.*;
import org.example.model.reservation.Reservation;
import org.example.model.user.User;
import org.example.repository.user.UserRepository;
import org.example.service.reservation.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;


    public ReservationController(ReservationService reservationService, UserRepository userRepository) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<ReservationResponseDto>> getAllReservationsByEmail(@PathVariable String email, Principal principal){
        String emailToken = principal.getName();
        if(!email.equals(emailToken)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(reservationService.getAllReservationsByEmail(email), HttpStatus.OK);
    }

    @PostMapping("/client")
    public ResponseEntity<ReservationResponseDto> createReservationClient(@RequestBody ReservationRequestDto requestDto, Principal principal){
        String emailToken = principal.getName();
        if(!requestDto.getEmail().equals(emailToken)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            ReservationResponseDto responseDto = reservationService.createReservation(requestDto);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        }
        catch(InsufficientFundsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch(ReservationTimeConflictException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/client/cancel/{id}")
    public ResponseEntity<BalanceOperationDTO> cancelReservationClient(@PathVariable Long id, Principal principal){
        String emailToken = principal.getName();
        try {
            BalanceOperationDTO response = reservationService.cancelReservationClient(id, emailToken);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch (AccessDeniedException e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        catch (ReservationNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (ReservationExpiredException | ReservationStatusWrongTypeException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/freeTables")
    public ResponseEntity<FindFreeTablesResponse> findAllFreeRestaurantTables(@RequestBody FindFreeTablesRequest request){
        return new ResponseEntity<>(reservationService.findAllFreeRestaurantTables(request), HttpStatus.OK);
    }

    @GetMapping("/employee/nextReservation")
    public ResponseEntity<List<NextReservationDTO>> getNextReservations(){
        return new ResponseEntity<>(reservationService.getNextReservations(), HttpStatus.OK);
    }

}
