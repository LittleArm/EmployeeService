package com.example.employeeservice.client;

import com.example.employeeservice.model.User;
import com.example.employeeservice.security.JwtAuthenticationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceClient {
    @Value("${application.service.url}")
    private String userServiceUrl;

    private final RestTemplate restTemplate;

    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public User getUserByEmail(String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken) {
            String jwtToken = ((JwtAuthenticationToken) authentication).getToken();
            String url = userServiceUrl + "/email?email=" + email;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<User> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    User.class
            );
            return response.getBody();
        }
        throw new IllegalStateException("JWT token not found in security context");
    }

    public void updateUser(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String jwtToken = ((JwtAuthenticationToken) authentication).getToken();
        String url = userServiceUrl + "/update";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> entity = new HttpEntity<>(user,headers);
        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                Void.class
        );
    }
}