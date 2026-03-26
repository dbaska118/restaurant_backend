package org.example.service.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.JwtService;
import org.example.dto.user.AuthResponse;
import org.example.dto.user.LoginRequest;
import org.example.exception.EmailInUseException;
import org.example.model.user.User;
import org.example.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public void register(User user) {
       try {
           if(userRepository.findByEmail(user.getEmail()).isPresent()) {
               throw new EmailInUseException();
           }
           user.setPassword(passwordEncoder.encode(user.getPassword()));
           userRepository.saveAndFlush(user);
       }
       catch (Exception e) {
           throw new EmailInUseException();
       }
    }

    public AuthResponse login(LoginRequest loginRequest, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        } catch (Exception e) {
            return null;
        }
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        String accessToken = jwtService.genertateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        if (loginRequest.getLogout()) {
            ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(Duration.ofDays(7))
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        }
        else {
            ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(-1)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        }
        return new AuthResponse(accessToken, user.getEmail(), user.getRole());
    }

    public void logout(HttpServletResponse response) {
        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    public AuthResponse refreshToken(HttpServletRequest request) {
        String refreshToken = null;
        if(request.getCookies() != null) {
            for(var cookie : request.getCookies()) {
                if(cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                }
            }
        }
        if(refreshToken == null) {
            return null;
        }
        String email = jwtService.extractUsername(refreshToken);
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()) {
            return null;
        }
        if(jwtService.isTokenValid(refreshToken, user.get())) {
            String accessToken = jwtService.genertateAccessToken(user.get());

            return new AuthResponse(accessToken, user.get().getEmail(), user.get().getRole());
        }
        else {
            return null;
        }
    }
}
