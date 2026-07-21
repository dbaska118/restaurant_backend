package org.example.security.dish;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class DishSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllDishesTestPublicAccess() throws Exception {
        mockMvc.perform(get("/api/dish"))
                .andExpect(status().isOk());
    }


    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin"})
    public void createDishTestAuthorized(String role) throws Exception {
        mockMvc.perform(post("/api/dish")
                        .with(user("user@wp.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "employee"})
    public void createDishTestForbiddenWrongRole(String role) throws Exception {
        mockMvc.perform(post("/api/dish")
                        .with(user("user@wp.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createDishTestUnauthorized() throws Exception {
        mockMvc.perform(post("/api/dish"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin"})
    public void deleteDishTestAuthorized(String role) throws Exception {
        mockMvc.perform(delete("/api/dish/2")
                        .with(user("user@wp.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "employee"})
    public void deleteDishTestForbiddenWrongRole(String role) throws Exception {
        mockMvc.perform(delete("/api/dish/2")
                        .with(user("user@wp.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteDishTestUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/dish/2"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin"})
    public void updateDishTestAuthorized(String role) throws Exception {
        mockMvc.perform(put("/api/dish/1")
                        .with(user("user@wp.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "employee"})
    public void updateDishTestForbiddenWrongRole(String role) throws Exception {
        mockMvc.perform(put("/api/dish/1")
                        .with(user("user@wp.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateDishTestUnauthorized() throws Exception {
        mockMvc.perform(put("/api/dish/1"))
                .andExpect(status().isUnauthorized());
    }





}
