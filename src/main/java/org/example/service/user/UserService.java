package org.example.service.user;

import org.example.dto.user.ChangeNameRequest;
import org.example.dto.user.ChangePasswordRequest;
import org.example.dto.user.NameResponse;
import org.example.exception.*;
import org.example.model.user.Admin;
import org.example.model.user.Client;
import org.example.model.user.User;
import org.example.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Double getBalance(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        if(user instanceof Client) {
            return ((Client) user).getBalance();
        }
        else {
           throw new NotClientException();
        }
    }

    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findByEmail(changePasswordRequest.getEmail())
                .orElseThrow(UserNotFoundException::new);

        if(passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userRepository.save(user);
        }
        else {
            throw new InvalidPasswordException();
        }
    }

    public List<User> getAllUsersAdmin() {
        return userRepository.findAllByRoleInOrderByIdAsc(List.of("client", "employee"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAllByRoleInOrderByIdAsc(List.of("client", "employee", "admin"));
    }

    public User deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        if(user instanceof Admin) {
            throw new IsAdminException();
        }
        userRepository.delete(user);
        return user;
    }

    public User deleteAdmin(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        if(user instanceof Admin) {
            userRepository.delete(user);
            return user;
        }
        throw new NotAdminException();
    }

    public User updateUser(long id, User user) {
        User userDB = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        if(user instanceof Admin) {
            throw new IsAdminException();
        }

        userDB.setFirstName(user.getFirstName());
        userDB.setLastName(user.getLastName());
        if(user.getPassword() != null) {
            userDB.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(userDB);
        return userDB;
    }

    public User updateAdmin(long id, User user) {
        User userDB = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        if(user instanceof Admin) {
            userDB.setFirstName(user.getFirstName());
            userDB.setLastName(user.getLastName());
            if(user.getPassword() != null) {
                userDB.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            return user;
        }
        throw new NotAdminException();
    }

    public void changeName(ChangeNameRequest changeNameRequest) {
        User user = userRepository.findByEmail(changeNameRequest.getEmail()).orElseThrow(UserNotFoundException::new);
        user.setFirstName(changeNameRequest.getFirstName());
        user.setLastName(changeNameRequest.getLastName());
        userRepository.save(user);
    }

    public NameResponse getName(String email){
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        NameResponse nameResponse = new NameResponse();
        nameResponse.setFirstName(user.getFirstName());
        nameResponse.setLastName(user.getLastName());
        return nameResponse;
    }
}
