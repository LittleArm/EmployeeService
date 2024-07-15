package com.example.employeeservice.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.employeeservice.dtos.*;

@Service
public class UserServiceClient {

    @Value("${application.service.url}")
    private String userServiceUrl;

    private final RestTemplate restTemplate;

    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public void registerEmployee(UserDto user, String jwtToken) {
        // Optional: Interact with user service if needed
        String url = userServiceUrl + "/auth/register";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDto> entity = new HttpEntity<>(user, headers);
        restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                Void.class
        );



    }

    public UserDto getUserByEmail(String email, String jwtToken) {
        System.out.println("Get in get2");
        String url = userServiceUrl + "/email?email=" + email;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<UserDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                UserDto.class
        );
        return response.getBody();
    }

    public void updateUser(UserDto user, String jwtToken) {
        String url = userServiceUrl + "/update";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDto> entity = new HttpEntity<>(user, headers);
        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                Void.class
        );
    }
    public UserDto getUserById(Long id, String jwtToken) {
        String url = String.format("%s/api/user/%d", userServiceUrl, id);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<UserDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                UserDto.class
        );
        return response.getBody();
    }


}
