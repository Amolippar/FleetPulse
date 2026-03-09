package com.fleetmanagement.backend.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. Check if the header contains a Bearer token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwt = authHeader.substring(7);
            userEmail = jwtService.extractEmail(jwt); // Your existing method

            // 2. If email exists and user is not yet authenticated in this request
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Note: In a production app, you'd verify the user still exists in the DB here
                // For now, we extract the role to set permissions
                String role = jwtService.extractRole(jwt); 
                
                // IMPORTANT: Add "ROLE_" prefix so .hasRole("ADMIN") works in SecurityConfig
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail,
                        null,
                        java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role))
                );

                authToken.setDetails(new org.springframework.security.web.authentication.WebAuthenticationDetailsSource().buildDetails(request));
                
                // 3. Finalize authentication for this request
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            // Token is invalid, expired, or malformed.
            // We log it or just ignore it so the filter chain continues.
            // If the endpoint is protected, Spring Security will deny access later.
            // If the endpoint is public (permitAll), the request will succeed.
        }
        
        filterChain.doFilter(request, response);
    }
}