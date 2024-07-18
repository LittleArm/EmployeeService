package com.example.employeeservice.clients;

import com.example.employeeservice.dtos.UserDTO;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceClient {
    @Value("${application.service.url}")
    private String userServiceUrl;

    private final RestTemplate restTemplate;

    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserDTO getUserByEmail(String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String jwtToken = ((JwtAuthenticationToken) authentication).getToken();
        String url = userServiceUrl + "/users/" + email;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<UserDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    UserDTO.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            // Handle client error (4xx codes)
            throw new IllegalStateException("User not found or bad request", e);
        } catch (HttpServerErrorException e) {
            // Handle server error (5xx codes)
            throw new IllegalStateException("Server error while fetching user", e);
        } catch (RestClientException e) {
            // Handle other errors such as connectivity issues
            throw new IllegalStateException("Error occurred while fetching user", e);
        }
    }

    public void updateUser(UserDTO userDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String jwtToken = ((JwtAuthenticationToken) authentication).getToken();
        String url = userServiceUrl + "/users";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDTO> entity = new HttpEntity<>(userDTO,headers);
        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                Void.class
        );
    }
}