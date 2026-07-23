package org.example.security.restaurantTable;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class RestaurantTableSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllRestaurantTablesTestPublicAccess() throws Exception {
        mockMvc.perform(get("/api/restaurantTable"))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin"})
    public void createRestaurantTableTestAuthorized(String role) throws Exception {
        mockMvc.perform(post("/api/restaurantTable")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "employee"})
    public void createRestaurantTableTestForbiddenWrongRole(String role) throws Exception {
        mockMvc.perform(post("/api/restaurantTable")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createRestaurantTableTestUnauthorized() throws Exception {
        mockMvc.perform(post("/api/restaurantTable"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin"})
    public void updateRestaurantTableTestAuthorized(String role) throws Exception {
        mockMvc.perform(put("/api/restaurantTable/4")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "employee"})
    public void updateRestaurantTableTestForbiddenWrongRole(String role) throws Exception {
        mockMvc.perform(put("/api/restaurantTable/4")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateRestaurantTableTestUnauthorized() throws Exception {
        mockMvc.perform(put("/api/restaurantTable/4"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin"})
    public void deleteRestaurantTableTestAuthorized(String role) throws Exception {
        mockMvc.perform(delete("/api/restaurantTable/4")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "employee"})
    public void deleteRestaurantTableTestForbiddenWrongRole(String role) throws Exception {
        mockMvc.perform(delete("/api/restaurantTable/4")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteRestaurantTablePriceTestUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/restaurantTable/4"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"employee"})
    public void updateRestaurantTableStatusTestAuthorized(String role) throws Exception {
        mockMvc.perform(patch("/api/restaurantTable/4/status")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "headAdmin", "admin"})
    public void updateRestaurantTableStatusTestForbiddenWrongRole(String role) throws Exception {
        mockMvc.perform(patch("/api/restaurantTable/4/status")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateRestaurantTableStatusTestUnauthorized() throws Exception {
        mockMvc.perform(patch("/api/restaurantTable/4/status"))
                .andExpect(status().isUnauthorized());
    }


}
