package com.example.employeeservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        final List<String> authority;
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        authority = jwtService.extractAuthorities(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;
            if (!authority.contains("ADMIN")) {
                userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            }
            if (jwtService.isTokenValid(jwt, userDetails)) {
                JwtAuthenticationToken authToken = new JwtAuthenticationToken(
                        userDetails,
                        null,
                        jwt,
                        authority.contains("ADMIN") ? List.of(() -> "ROLE_ADMIN") : userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        if (request.getServletPath().contains("/register") || request.getServletPath().contains("/delete")) {
            if (authority.contains("ADMIN")){
                filterChain.doFilter(request, response);
                return;
            }
            throw new IllegalStateException("No authority");
        }

        filterChain.doFilter(request, response);
    }
}
