package org.example;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Autowired
    public SecurityConfig(JwtAuthFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()


                        .requestMatchers("/api/user/balance/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/user/changePassword/**").authenticated()
                        .requestMatchers("/api/user/name/**").authenticated()
                        .requestMatchers("/api/user/headAdmin/**").hasAnyAuthority("headAdmin")
                        .requestMatchers("/api/user/**").hasAnyAuthority("admin", "headAdmin")

                        .requestMatchers(HttpMethod.GET, "/api/dish/**").permitAll()
                        .requestMatchers("/api/dish/**").hasAnyAuthority("admin", "headAdmin")

                        .requestMatchers(HttpMethod.GET, "/api/tablePrice/**").permitAll()
                        .requestMatchers("/api/tablePrice/**").hasAnyAuthority("admin", "headAdmin")

                        .requestMatchers(HttpMethod.GET, "/api/restaurantTable/**").permitAll()
                        .requestMatchers("/api/restaurantTable/**").hasAnyAuthority("admin", "headAdmin")

                        .requestMatchers(HttpMethod.POST, "/api/balanceOperation/employee/**").hasAnyAuthority("employee")
                        .requestMatchers("api/balanceOperation/**").hasAnyAuthority("client")

                        .requestMatchers(HttpMethod.GET, "/api/openingHours/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/openingHours/**").hasAnyAuthority("admin", "headAdmin")



                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                        .accessDeniedHandler((request, response, accessDeniedException) -> {response.setStatus(HttpStatus.FORBIDDEN.value());response.setContentType("application/json");})
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
