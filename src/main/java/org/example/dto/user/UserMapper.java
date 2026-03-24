package org.example.dto.user;

import org.example.model.user.Client;
import org.example.model.user.User;

public class UserMapper {

    public UserDtoResponse fromUser(User user) {
        UserDtoResponse userDtoResponse = null;
        if (user.getRole() == "client") {
            userDtoResponse = new ClientDtoResponse();
            userDtoResponse.setId(user.getId());
            userDtoResponse.setEmail(user.getEmail());
            userDtoResponse.setFirstName(user.getFirstName());
            userDtoResponse.setLastName(user.getLastName());
            userDtoResponse.setRole(user.getRole());
            ((ClientDtoResponse)userDtoResponse).setBalance(((Client) user).getBalance());
        }
        else if (user.getRole() == "employee") {
            userDtoResponse = new EmployeeDtoResponse();
            userDtoResponse.setId(user.getId());
            userDtoResponse.setEmail(user.getEmail());
            userDtoResponse.setFirstName(user.getFirstName());
            userDtoResponse.setLastName(user.getLastName());
            userDtoResponse.setRole(user.getRole());
        }
        else if (user.getRole() == "admin") {
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
