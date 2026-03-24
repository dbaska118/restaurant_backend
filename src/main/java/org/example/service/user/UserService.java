package org.example.service.user;

import org.example.model.user.Client;
import org.example.model.user.User;
import org.example.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        User user = userRepository.findByEmail(email).get();
        if(user.getRole().equals("client")) {
            return ((Client) user).getBalance();
        }
        else {
            return null;
        }
    }

    
}
