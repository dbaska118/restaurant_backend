package org.example.security.reservation;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class OpeningHoursSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getOpeningHoursTestPublicAccess() throws Exception {
        mockMvc.perform(get("/api/openingHours"))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin"})
    public void updateOpeningHoursAuthorized(String role) throws Exception {
        mockMvc.perform(put("/api/openingHours/test")
                        .with(user("user@wp.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"client", "employee"})
    public void updateOpeningHoursForbiddenWrongRole(String role) throws Exception {
        mockMvc.perform(put("/api/openingHours/test")
                        .with(user("user@wp.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateOpeningHoursUnauthorized() throws Exception {
        mockMvc.perform(put("/api/openingHours/test"))
                .andExpect(status().isUnauthorized());
    }
}
