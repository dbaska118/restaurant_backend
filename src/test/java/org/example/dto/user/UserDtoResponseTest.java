package org.example.dto.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserDtoResponseTest {

    @Test
    public void setTest() {
        UserDtoResponse userDtoResponse = new AdminDtoResponse();
        userDtoResponse.setId(-1L);
        userDtoResponse.setEmail("admin@wp.pl");
        userDtoResponse.setFirstName("Jan");
        userDtoResponse.setLastName("Nowak");
        userDtoResponse.setRole("admin");

        Assertions.assertEquals(-1L, userDtoResponse.getId());
        Assertions.assertEquals("admin@wp.pl", userDtoResponse.getEmail());
        Assertions.assertEquals("Jan", userDtoResponse.getFirstName());
        Assertions.assertEquals("Nowak", userDtoResponse.getLastName());
        Assertions.assertEquals("admin", userDtoResponse.getRole());
    }
}
