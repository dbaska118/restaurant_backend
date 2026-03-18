package org.example.model.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EmployeeTest {

    @Test
    public void argsConstructorTest(){
        Employee employee = new Employee("user@wp.pl", "password", "Jan", "Nowak");
        Assertions.assertEquals("employee", employee.getRole());
    }

    @Test
    public void noArgsConstructorTest(){
        User user = new Employee();
        Assertions.assertEquals("employee", user.getRole());
    }
}
