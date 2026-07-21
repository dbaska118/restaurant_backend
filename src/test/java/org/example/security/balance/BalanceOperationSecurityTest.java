package org.example.security.balance;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.balance.AddBalanceRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BalanceOperationSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addFundsTestAuthorized() throws Exception {
        AddBalanceRequest request = new AddBalanceRequest("client@test.pl", 50);

        mockMvc.perform(post("/api/balanceOperation/addFunds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(user("client@test.pl").authorities(new SimpleGrantedAuthority("client"))))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addFundsTestForbiddenWrongUser() throws Exception {
        AddBalanceRequest request = new AddBalanceRequest("client@test.pl", 50);

        mockMvc.perform(post("/api/balanceOperation/addFunds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority("client"))))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin", "employee"})
    public void addFundsTestForbiddenWrongRole(String role) throws Exception {
        mockMvc.perform(post("/api/balanceOperation/addFunds")
                        .with(user("user@wp.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void addFundsTestUnauthorized() throws Exception {
        mockMvc.perform(post("/api/balanceOperation/addFunds"))
                .andExpect(status().isUnauthorized());
    }



    @Test
    public void getAllBalanceOperationsTestAuthorized() throws Exception {
        mockMvc.perform(get("/api/balanceOperation/client@test.pl")
                    .with(user("client@test.pl").authorities(new SimpleGrantedAuthority("client"))))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllBalanceOperationsTestForbiddenWrongUser() throws Exception {
        mockMvc.perform(get("/api/balanceOperation/client@test.pl")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority("client"))))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin", "employee"})
    public void getAllBalanceOperationsTestForbiddenWrongRole(String role) throws Exception {
        mockMvc.perform(get("/api/balanceOperation/client@test.pl")
                        .with(user("user@wp.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getAllBalanceOperationsTestUnauthorized() throws Exception {
        mockMvc.perform(get("/api/balanceOperation/client@test.pl"))
                .andExpect(status().isUnauthorized());
    }




    @Test
    public void addFundsEmployeeTestAuthorized() throws Exception {
        mockMvc.perform(post("/api/balanceOperation/employee/addFunds")
                        .with(user("user@wp.pl").authorities(new SimpleGrantedAuthority("employee"))))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin", "client"})
    public void addFundsEmployeeTestForbiddenWrongRole(String role) throws Exception {
        mockMvc.perform(post("/api/balanceOperation/employee/addFunds")
                        .with(user("user@wp.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void addFundsEmployeeTestUnauthorized() throws Exception {
        mockMvc.perform(post("/api/balanceOperation/employee/addFunds"))
                .andExpect(status().isUnauthorized());
    }




}
