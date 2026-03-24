package org.example.dto.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClientDtoResponseTest {

    @Test
    public void setTest(){
        UserDtoResponse userDtoResponse = new ClientDtoResponse();
        ((ClientDtoResponse)userDtoResponse).setBalance(500);

        Assertions.assertEquals(500, ((ClientDtoResponse)userDtoResponse).getBalance());
    }
}
