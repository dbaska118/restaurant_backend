package org.example.dto.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AuthResponseTest {

    @Test
    public void getTest(){
        AuthResponse authResponse = new AuthResponse("token", "admin@wp.pl", "admin");
        Assertions.assertEquals("token", authResponse.getToken());
        Assertions.assertEquals("admin@wp.pl", authResponse.getEmail());
        Assertions.assertEquals("admin", authResponse.getRole());
    }

    @Test
    public void setTest(){
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken("token");
        authResponse.setEmail("admin@wp.pl");
        authResponse.setRole("admin");

        Assertions.assertEquals("token", authResponse.getToken());
        Assertions.assertEquals("admin@wp.pl", authResponse.getEmail());
        Assertions.assertEquals("admin", authResponse.getRole());
    }
}
