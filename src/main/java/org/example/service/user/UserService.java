package org.example.service.user;

import org.example.dto.user.ChangePasswordRequest;
import org.example.exception.InvalidPasswordException;
import org.example.exception.NotClientException;
import org.example.exception.UserNotFoundException;
import org.example.model.user.Client;
import org.example.model.user.User;
import org.example.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    
}
