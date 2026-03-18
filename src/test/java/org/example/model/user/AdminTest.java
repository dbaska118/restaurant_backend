package org.example.model.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class AdminTest {

    @Test
    public void argsConstructorTest(){
        User admin = new Admin("user@wp.pl", "password", "Jan", "Nowak");
        Assertions.assertEquals("admin", admin.getRole());
    }

    @Test
    public void noArgsConstructorTest(){
        User admin = new Admin();
        Assertions.assertEquals("admin", admin.getRole());
    }
}
