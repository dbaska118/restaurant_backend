package org.example.dto.user;

import org.example.model.user.Admin;
import org.example.model.user.Client;
import org.example.model.user.Employee;
import org.example.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDtoResponse fromUser(User user) {
        UserDtoResponse userDtoResponse = null;
        if (user instanceof Client) {
            userDtoResponse = new ClientDtoResponse();
            userDtoResponse.setId(user.getId());
            userDtoResponse.setEmail(user.getEmail());
            userDtoResponse.setFirstName(user.getFirstName());
            userDtoResponse.setLastName(user.getLastName());
            userDtoResponse.setRole(user.getRole());
            ((ClientDtoResponse)userDtoResponse).setBalance(((Client) user).getBalance());
        }
        else if (user instanceof Employee) {
            userDtoResponse = new EmployeeDtoResponse();
            userDtoResponse.setId(user.getId());
            userDtoResponse.setEmail(user.getEmail());
            userDtoResponse.setFirstName(user.getFirstName());
            userDtoResponse.setLastName(user.getLastName());
            userDtoResponse.setRole(user.getRole());
        }
        else if (user instanceof Admin) {
            userDtoResponse = new AdminDtoResponse();
            userDtoResponse.setId(user.getId());
            userDtoResponse.setEmail(user.getEmail());
            userDtoResponse.setFirstName(user.getFirstName());
            userDtoResponse.setLastName(user.getLastName());
            userDtoResponse.setRole(user.getRole());
        }
        return userDtoResponse;
    }
}
