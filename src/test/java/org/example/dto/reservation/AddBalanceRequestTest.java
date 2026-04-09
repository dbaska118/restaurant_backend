package org.example.dto.reservation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AddBalanceRequestTest {

    @Test
    public void getTest(){
        AddBalanceRequest addBalanceRequest = new AddBalanceRequest("client@wp.pl", 123.45);

        Assertions.assertEquals("client@wp.pl", addBalanceRequest.getEmail());
        Assertions.assertEquals(123.45, addBalanceRequest.getAmount());
    }
}
