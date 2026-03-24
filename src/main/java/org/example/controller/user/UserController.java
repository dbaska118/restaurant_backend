package org.example.controller.user;


import org.example.dto.user.ChangePasswordRequest;
import org.example.exception.InvalidPasswordException;
import org.example.exception.NotClientException;
import org.example.exception.UserNotFoundException;
import org.example.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/balance/{email}")
    public ResponseEntity<Double> getBalance(@PathVariable String email) {
        try {
            double balance = userService.getBalance(email);
            return new ResponseEntity<>(balance, HttpStatus.OK);
        }
        catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (NotClientException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            userService.changePassword(changePasswordRequest);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (InvalidPasswordException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
