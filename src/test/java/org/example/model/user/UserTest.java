package org.example.model.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void getTest(){
        User user = new Admin("admin@wp.pl", "password", "Jan", "Nowak");
        Assertions.assertEquals("admin@wp.pl", user.getEmail());
        Assertions.assertEquals("password", user.getPassword());
        Assertions.assertEquals("Jan", user.getFirstName());
        Assertions.assertEquals("Nowak", user.getLastName());
        Assertions.assertEquals("admin", user.getRole());
        Assertions.assertTrue(user.isEnabled());
    }

    @Test
    public void setTest(){
        User user = new Admin();
        user.setEmail("admin@wp.pl");
        user.setPassword("password");
        user.setFirstName("Jan");
        user.setLastName("Nowak");
        user.setRole("role");
        user.setEnabled(false);

        Assertions.assertEquals("admin@wp.pl", user.getEmail());
        Assertions.assertEquals("password", user.getPassword());
        Assertions.assertEquals("Jan", user.getFirstName());
        Assertions.assertEquals("Nowak", user.getLastName());
        Assertions.assertEquals("role", user.getRole());
        Assertions.assertFalse(user.isEnabled());

    }
}
