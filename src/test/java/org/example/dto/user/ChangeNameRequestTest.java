package org.example.dto.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ChangeNameRequestTest {

    @Test
    public void setTest(){
        ChangeNameRequest request = new ChangeNameRequest();
        request.setEmail("test@test.com");
        request.setFirstName("Jan");
        request.setLastName("Nowak");

        Assertions.assertEquals("test@test.com", request.getEmail());
        Assertions.assertEquals("Jan", request.getFirstName());
        Assertions.assertEquals("Nowak", request.getLastName());
    }
}
