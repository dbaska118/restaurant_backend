package org.example.security.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.reservation.ReservationRequestDto;
import org.example.model.reservation.Reservation;
import org.example.model.user.Client;
import org.example.model.user.User;
import org.example.repository.reservation.ReservationRepository;
import org.example.repository.user.UserRepository;
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
public class ReservationSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReservationRepository reservationRepository;


    @Test
    public void getAllReservationsByEmailTestAuthorized() throws Exception {
        mockMvc.perform(get("/api/reservation/user@wp.pl")
                        .with(user("user@wp.pl").authorities(new SimpleGrantedAuthority("client"))))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllReservationsByEmailTestForbiddenWrongUser() throws Exception {
        mockMvc.perform(get("/api/reservation/user@wp.pl")
                        .with(user("client@wp.pl").authorities(new SimpleGrantedAuthority("client"))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getAllReservationsByEmailTestUnauthorized() throws Exception {
        mockMvc.perform(get("/api/reservation/user@wp.pl"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void findAllFreeRestaurantTablesTestPublicAccess() throws Exception {
        mockMvc.perform(post("/api/reservation/freeTables"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createReservationClientTestAuthorized() throws Exception {
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto();
        reservationRequestDto.setEmail("client@wp.pl");

        mockMvc.perform(post("/api/reservation/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationRequestDto))
                        .with(user("client@wp.pl").authorities(new SimpleGrantedAuthority("client"))))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createReservationClientTestForbiddenWrongUser() throws Exception {
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto();
        reservationRequestDto.setEmail("client@wp.pl");

        mockMvc.perform(post("/api/reservation/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationRequestDto))
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority("client"))))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin", "employee"})
    public void createReservationClientTestForbiddenWrongRole(String Role) throws Exception {
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto();
        reservationRequestDto.setEmail("user@test.pl");

        mockMvc.perform(post("/api/reservation/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationRequestDto))
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(Role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createReservationClientTestUnauthorized() throws Exception {
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto();
        reservationRequestDto.setEmail("user@test.pl");

        mockMvc.perform(post("/api/reservation/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationRequestDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void cancelReservationClientTestAuthorized() throws Exception {

        Reservation reservation = new Reservation();
        reservation.setEmail("client@wp.pl");
        reservationRepository.save(reservation);

        mockMvc.perform(post("/api/reservation/client/cancel/{id}", reservation.getId())
                        .with(user("client@wp.pl").authorities(new SimpleGrantedAuthority("client"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void cancelReservationClientTestForbiddenWrongUser() throws Exception {

        Reservation reservation = new Reservation();
        reservation.setEmail("client@wp.pl");
        reservationRepository.save(reservation);

        mockMvc.perform(post("/api/reservation/client/cancel/{id}", reservation.getId())
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority("client"))))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin", "employee"})
    public void cancelReservationClientTestForbiddenWrongRole(String Role) throws Exception {

        Reservation reservation = new Reservation();
        reservation.setEmail("client@wp.pl");
        reservationRepository.save(reservation);

        mockMvc.perform(post("/api/reservation/client/cancel/{id}", reservation.getId())
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(Role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void cancelReservationClientTestUnauthorized() throws Exception {

        Reservation reservation = new Reservation();
        reservation.setEmail("client@wp.pl");
        reservationRepository.save(reservation);

        mockMvc.perform(post("/api/reservation/client/cancel/{id}", reservation.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getNextReservationsTestAuthorized() throws Exception {
        mockMvc.perform(get("/api/reservation/employee/nextReservation")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority("employee"))))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin", "client"})
    public void getNextReservationsTestForbiddenWrongRole(String role) throws Exception {
        mockMvc.perform(get("/api/reservation/employee/nextReservation")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getNextReservationsTestUnauthorized() throws Exception {
        mockMvc.perform(get("/api/reservation/employee/nextReservation"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getTodayReservationsByEmailTestAuthorized() throws Exception {
        mockMvc.perform(get("/api/reservation/employee/client@wp.pl")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority("employee"))))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin", "client"})
    public void getTodayReservationsByEmailTestForbiddenWrongRole(String role) throws Exception {
        mockMvc.perform(get("/api/reservation/employee/client@wp.pl")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getTodayReservationsByEmailTestUnauthorized() throws Exception {
        mockMvc.perform(get("/api/reservation/employee/client@wp.pl"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void startReservationTestAuthorized() throws Exception {
        mockMvc.perform(post("/api/reservation/employee/startReservation")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority("employee"))))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"headAdmin", "admin", "client"})
    public void startReservationTestForbiddenWrongRole(String role) throws Exception {
        mockMvc.perform(post("/api/reservation/employee/startReservation")
                        .with(user("user@test.pl").authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().isForbidden());
    }

    @Test
    public void startReservationTestUnauthorized() throws Exception {
        mockMvc.perform(post("/api/reservation/employee/startReservation"))
                .andExpect(status().isUnauthorized());
    }


}
