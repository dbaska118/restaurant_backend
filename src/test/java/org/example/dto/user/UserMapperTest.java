package org.example.dto.user;

import org.example.model.user.Admin;
import org.example.model.user.Client;
import org.example.model.user.Employee;
import org.example.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserMapperTest {

    @Test
    public void fromAdminTest(){
        UserMapper mapper = new UserMapper();
        User admin = new Admin("admin@wp.pl", "passwor", "Jan", "Nowak");
        UserDtoResponse userDtoResponse = mapper.fromUser(admin);

        Assertions.assertTrue(userDtoResponse instanceof AdminDtoResponse);
        Assertions.assertEquals("admin@wp.pl", userDtoResponse.getEmail());
        Assertions.assertEquals("Jan", userDtoResponse.getFirstName());
        Assertions.assertEquals("Nowak", userDtoResponse.getLastName());
        Assertions.assertEquals("admin", userDtoResponse.getRole());
    }

    @Test
    public void fromEmployeeTest(){
        UserMapper mapper = new UserMapper();
        User employee = new Employee("employee@wp.pl", "passwor", "Tomasz", "Kowalski");
        UserDtoResponse userDtoResponse = mapper.fromUser(employee);
        Assertions.assertTrue(userDtoResponse instanceof EmployeeDtoResponse);
        Assertions.assertEquals("employee@wp.pl", userDtoResponse.getEmail());
        Assertions.assertEquals("Tomasz", userDtoResponse.getFirstName());
        Assertions.assertEquals("Kowalski", userDtoResponse.getLastName());
        Assertions.assertEquals("employee", userDtoResponse.getRole());
    }

    @Test
    public void fromClientTest(){
        UserMapper mapper = new UserMapper();
        User client = new Client("client@wp.pl", "passwor", "Andrzej", "Kowal");
        ((Client) client).setBalance(200);

        UserDtoResponse userDtoResponse = mapper.fromUser(client);
        Assertions.assertTrue(userDtoResponse instanceof ClientDtoResponse);
        Assertions.assertEquals("client@wp.pl", userDtoResponse.getEmail());
        Assertions.assertEquals("Andrzej", userDtoResponse.getFirstName());
        Assertions.assertEquals("Kowal", userDtoResponse.getLastName());
        Assertions.assertEquals("client", userDtoResponse.getRole());
        Assertions.assertEquals(200, ((ClientDtoResponse) userDtoResponse).getBalance());
    }
}
