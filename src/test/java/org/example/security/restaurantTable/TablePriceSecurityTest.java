package org.example.security.restaurantTable;


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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TablePriceSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllTablePriceTestPublicAccess() throws Exception {
        mockMvc.perform(get("/api/tablePrice"))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin"})
    public void createTablePriceTestAuthorized(String role) throws Exception {
        mockMvc.perform(post("/api/tablePrice")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "employee"})
    public void createTablePriceTestForbiddenWrongRole(String role) throws Exception {
        mockMvc.perform(post("/api/tablePrice")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createTablePriceTestUnauthorized() throws Exception {
        mockMvc.perform(post("/api/tablePrice"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin"})
    public void updateTablePriceTestAuthorized(String role) throws Exception {
        mockMvc.perform(put("/api/tablePrice/4")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "employee"})
    public void updateTablePriceTestForbiddenWrongRole(String role) throws Exception {
        mockMvc.perform(put("/api/tablePrice/4")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateTablePricePriceTestUnauthorized() throws Exception {
        mockMvc.perform(put("/api/tablePrice/4"))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin"})
    public void deleteTablePriceTestAuthorized(String role) throws Exception {
        mockMvc.perform(delete("/api/tablePrice/4")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "employee"})
    public void deleteTablePriceTestForbiddenWrongRole(String role) throws Exception {
        mockMvc.perform(delete("/api/tablePrice/4")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteTablePricePriceTestUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/tablePrice/4"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getPossibleNumberOfChairsTestPublicAccess() throws Exception {
        mockMvc.perform(get("/api/tablePrice/possibleNumberOfChairs"))
                .andExpect(status().isOk());
    }
}
