package com.example.employeeservice.client;

import com.example.employeeservice.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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

    public User getUserByEmail(String email, String jwtToken) {
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
}