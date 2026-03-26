package org.example.controller.user;


import org.example.dto.user.ChangePasswordRequest;
import org.example.dto.user.UserDtoResponse;
import org.example.dto.user.UserMapper;
import org.example.exception.*;
import org.example.model.user.Admin;
import org.example.model.user.Client;
import org.example.model.user.Employee;
import org.example.model.user.User;
import org.example.service.user.AuthService;
import org.example.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, AuthService authService, UserMapper userMapper) {
        this.userService = userService;
        this.authService = authService;
        this.userMapper = userMapper;
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

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.getAllUsersAdmin(), HttpStatus.OK);
    }

    @GetMapping("/headAdmin")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDtoResponse> addUser(@RequestBody User user) {
        if(user instanceof Admin){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(user instanceof Client){
            user.setRole("client");
        }
        else if(user instanceof Employee){
            user.setRole("employee");
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            authService.register(user);
            return new ResponseEntity<>(userMapper.fromUser(user), HttpStatus.CREATED);
        }
        catch (EmailInUseException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/headAdmin")
    public ResponseEntity<UserDtoResponse> addAdmin(@RequestBody User user) {
        if(user instanceof Admin){
            user.setRole("admin");
            try {
                authService.register(user);
                return new ResponseEntity<>(userMapper.fromUser(user), HttpStatus.CREATED);
            }
            catch (EmailInUseException e) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDtoResponse> deleteUser(@PathVariable Long id ) {
        try {
            User userDB =  userService.deleteUser(id);
            return new ResponseEntity<>(userMapper.fromUser(userDB), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (IsAdminException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/headAdmin/{id}")
    public ResponseEntity<UserDtoResponse> deleteAdmin(@PathVariable Long id ) {
        try {
            User userDB = userService.deleteAdmin(id);
            return new ResponseEntity<>(userMapper.fromUser(userDB), HttpStatus.OK);
        }
        catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (NotAdminException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDtoResponse> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User userDB = userService.updateUser(id, user);
            return new ResponseEntity<>(userMapper.fromUser(userDB), HttpStatus.OK);
        }
        catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (IsAdminException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/headAdmin/{id}")
    public ResponseEntity<UserDtoResponse> updateAdmin(@PathVariable Long id, @RequestBody User user) {
        try {
            User userDB = userService.updateAdmin(id, user);
            return new ResponseEntity<>(userMapper.fromUser(userDB), HttpStatus.OK);
        }
        catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (NotAdminException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
