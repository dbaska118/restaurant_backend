package org.example.dto.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NameResponseTest {

    @Test
    public void setTest(){
        NameResponse nameResponse = new NameResponse();
        nameResponse.setFirstName("Jan");
        nameResponse.setLastName("Nowak");

        Assertions.assertEquals("Jan", nameResponse.getFirstName());
        Assertions.assertEquals("Nowak", nameResponse.getLastName());
    }
}
