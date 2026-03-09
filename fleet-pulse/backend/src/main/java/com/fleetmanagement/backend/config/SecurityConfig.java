package com.fleetmanagement.backend.config;

import com.fleetmanagement.backend.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    // Use Constructor Injection (Removed field @Autowired for best practice)
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. Disable CSRF (Necessary for APIs & Postman testing)
            .csrf(csrf -> csrf.disable())
            
            // 2. Configure CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 3. Set Session Policy to Stateless (Standard for JWT)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 4. Request Authorization Rules
            .authorizeHttpRequests(auth -> auth
                // Public endpoints (No token needed)
                .requestMatchers("/api/home/**", "/api/auth/**").permitAll()
                
                // Whitelist your Estimation endpoint for testing
                .requestMatchers("/api/estimations/**").permitAll() 
                
                // Protected endpoints
                .requestMatchers("/api/bookings/**").authenticated()
                .requestMatchers("/api/drivers/**").authenticated()
                .requestMatchers("/api/users/**").authenticated()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                // Everything else requires a valid JWT
                .anyRequest().authenticated()
            )
            
            // 5. Add JWT Filter before the standard UsernamePassword filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173", "http://localhost:5174"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cache-Control"));
        config.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}