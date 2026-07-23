package org.example.security.user;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.user.ChangeNameRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin", "client", "employee"})
    public void getBalanceTestAuthorized(String role) throws Exception {
        mockMvc.perform(get("/api/user/balance/user@test.pl")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getBalanceTestUnauthorized() throws Exception {
        mockMvc.perform(get("/api/user/balance/user@test.pl"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin", "client", "employee"})
    public void getNameTestAuthorized(String role) throws Exception {
        mockMvc.perform(get("/api/user/name/user@test.pl")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin", "client", "employee"})
    public void getNameTestForbiddenWrongUser(String role) throws Exception {
        mockMvc.perform(get("/api/user/name/user@test.pl")
                        .with(user("client@wp.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getNameTestUnauthorized() throws Exception {
        mockMvc.perform(get("/api/user/name/user@test.pl"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin", "client", "employee"})
    public void changeNameTestAuthorized(String role) throws Exception {
        ChangeNameRequest request = new ChangeNameRequest();
        request.setEmail("user@test.pl");

        mockMvc.perform(post("/api/user/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin", "client", "employee"})
    public void changeNameTestForbiddenWrongUser(String role) throws Exception {
        ChangeNameRequest request = new ChangeNameRequest();
        request.setEmail("user@test.pl");

        mockMvc.perform(post("/api/user/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(user("client@wp.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void changeNameTestUnauthorized() throws Exception {
        ChangeNameRequest request = new ChangeNameRequest();
        request.setEmail("user@test.pl");

        mockMvc.perform(post("/api/user/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin", "client", "employee"})
    public void changePasswordTestAuthorized(String role) throws Exception {
        mockMvc.perform(post("/api/user/changePassword")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void changePasswordTestUnauthorized() throws Exception {
        mockMvc.perform(post("/api/user/changePassword"))
                .andExpect(status().isUnauthorized());
    }


    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin"})
    public void getUsersTestAuthorized(String role) throws Exception {
        mockMvc.perform(get("/api/user")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "employee"})
    public void getUsersTestForbiddenWrongUser(String role) throws Exception {
        mockMvc.perform(get("/api/user")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getUsersTestUnauthorized() throws Exception {
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin"})
    public void getAllUsersTestAuthorized(String role) throws Exception {
        mockMvc.perform(get("/api/user/headAdmin")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "employee", "admin"})
    public void getAllUsersTestForbiddenWrongUser(String role) throws Exception {
        mockMvc.perform(get("/api/user/headAdmin")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getAllUsersTestUnauthorized() throws Exception {
        mockMvc.perform(get("/api/user/headAdmin"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin"})
    public void addUserTestAuthorized(String role) throws Exception {
        mockMvc.perform(post("/api/user")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "employee"})
    public void addUserTestForbiddenWrongUser(String role) throws Exception {
        mockMvc.perform(post("/api/user")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void addUserTestUnauthorized() throws Exception {
        mockMvc.perform(post("/api/user"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin"})
    public void addAdminTestAuthorized(String role) throws Exception {
        mockMvc.perform(post("/api/user/headAdmin")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "employee", "admin"})
    public void addAdminTestForbiddenWrongUser(String role) throws Exception {
        mockMvc.perform(post("/api/user/headAdmin")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void addAdminTestUnauthorized() throws Exception {
        mockMvc.perform(post("/api/user/headAdmin"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin"})
    public void deleteUserTestAuthorized(String role) throws Exception {
        mockMvc.perform(delete("/api/user/1")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "employee"})
    public void deleteUserTestForbiddenWrongUser(String role) throws Exception {
        mockMvc.perform(delete("/api/user/1")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteUserTestUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin"})
    public void deleteAdminTestAuthorized(String role) throws Exception {
        mockMvc.perform(delete("/api/user/headAdmin/1")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "employee", "admin"})
    public void deleteAdminTestForbiddenWrongUser(String role) throws Exception {
        mockMvc.perform(delete("/api/user/headAdmin/1")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteAdminTestUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/user/headAdmin/1"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin"})
    public void updateUserTestAuthorized(String role) throws Exception {
        mockMvc.perform(put("/api/user/1")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "employee"})
    public void updateUserTestForbiddenWrongUser(String role) throws Exception {
        mockMvc.perform(put("/api/user/1")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateUserTestUnauthorized() throws Exception {
        mockMvc.perform(put("/api/user/1"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin"})
    public void updateAdminTestAuthorized(String role) throws Exception {
        mockMvc.perform(put("/api/user/headAdmin/1")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "employee", "admin"})
    public void updateAdminTestForbiddenWrongUser(String role) throws Exception {
        mockMvc.perform(put("/api/user/headAdmin/1")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateAdminTestUnauthorized() throws Exception {
        mockMvc.perform(put("/api/user/headAdmin/1"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"employee"})
    public void findClientByEmailTestAuthorized(String role) throws Exception {
        mockMvc.perform(get("/api/user/employee/findClient/client@wp.pl")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "headAdmin", "admin"})
    public void findClientByEmailTestForbiddenWrongUser(String role) throws Exception {
        mockMvc.perform(get("/api/user/employee/findClient/client@wp.pl")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void findClientByEmailTestUnauthorized() throws Exception {
        mockMvc.perform(get("/api/user/employee/findClient/client@wp.pl"))
                .andExpect(status().isUnauthorized());
    }
}
