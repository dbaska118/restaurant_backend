package org.example.dto.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LoginRequestTest {

    @Test
    public void getTest(){
        LoginRequest loginRequest = new LoginRequest("admin@wp.pl", "password", true);
        Assertions.assertEquals("admin@wp.pl", loginRequest.getEmail());
        Assertions.assertEquals("password", loginRequest.getPassword());
        Assertions.assertTrue(loginRequest.getLogout());
    }

    @Test
    public void setTest(){
        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setEmail("admin@wp.pl");
        loginRequest.setPassword("password");
        loginRequest.setLogout(true);

        Assertions.assertEquals("admin@wp.pl", loginRequest.getEmail());
        Assertions.assertEquals("password", loginRequest.getPassword());
        Assertions.assertTrue(loginRequest.getLogout());
    }
}
