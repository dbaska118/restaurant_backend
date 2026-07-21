package org.example.security.balance;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.balance.AddBalanceRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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
    @WithMockUser(username = "client@test.pl", authorities = {"client"})
    public void addFundsTestAuthorized() throws Exception {

        AddBalanceRequest request = new AddBalanceRequest("client@test.pl", 50);

        mockMvc.perform(post("/api/balanceOperation/addFunds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user@test.pl", authorities = {"client"})
    public void addFundsTestForbiddenWrongUser() throws Exception {
        AddBalanceRequest request = new AddBalanceRequest("client@test.pl", 50);

        mockMvc.perform(post("/api/balanceOperation/addFunds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"employee", "admin", "headAdmin"})
    public void addFundsTestForbiddenWrongRole() throws Exception {
        mockMvc.perform(post("/api/balanceOperation/addFunds"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void addFundsTestUnauthorized() throws Exception {
        mockMvc.perform(post("/api/balanceOperation/addFunds"))
                .andExpect(status().isUnauthorized());
    }



    @Test
    @WithMockUser(username = "client@test.pl", authorities = {"client"})
    public void getAllBalanceOperationsTestAuthorized() throws Exception {
        mockMvc.perform(get("/api/balanceOperation/client@test.pl"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = {"client"})
    public void getAllBalanceOperationsTestForbiddenWrongUser() throws Exception {
        mockMvc.perform(get("/api/balanceOperation/client@test.pl"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"employee", "admin", "headAdmin"})
    public void getAllBalanceOperationsTestForbiddenWrongRole() throws Exception {
        mockMvc.perform(get("/api/balanceOperation/client@test.pl"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getAllBalanceOperationsTestUnauthorized() throws Exception {
        mockMvc.perform(get("/api/balanceOperation/client@test.pl"))
                .andExpect(status().isUnauthorized());
    }




    @Test
    @WithMockUser(authorities = {"employee"})
    public void addFundsEmployeeTestAuthorized() throws Exception {
        mockMvc.perform(post("/api/balanceOperation/employee/addFunds"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"client", "admin", "headAdmin"})
    public void addFundsEmployeeTestForbiddenWrongRole() throws Exception {
        mockMvc.perform(post("/api/balanceOperation/employee/addFunds"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void addFundsEmployeeTestUnauthorized() throws Exception {
        mockMvc.perform(post("/api/balanceOperation/employee/addFunds"))
                .andExpect(status().isUnauthorized());
    }




}
