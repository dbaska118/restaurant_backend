package org.example.model.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ClientTest {

    @Test
    public void argsConstructorTest(){
        User user = new Client("user@wp.pl", "password", "Jan", "Nowak");
        Assertions.assertEquals("client", user.getRole());
        Assertions.assertEquals(0, ((Client)user).getBalance());
    }

    @Test
    public void noArgsConstructorTest(){
        User user = new Client();
        Assertions.assertEquals("client", user.getRole());
        Assertions.assertEquals(0, ((Client)user).getBalance());
    }
}
