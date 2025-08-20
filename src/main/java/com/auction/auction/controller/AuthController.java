package com.auction.auction.controller;

import com.auction.auction.dto.UserLoginRequest;
import com.auction.auction.dto.UserRegistrationRequest;
import com.auction.auction.dto.UserResponse;
import com.auction.auction.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;

    @PostMapping("/users")
    public UserResponse registerUser(@RequestBody UserRegistrationRequest request) {
        log.info("Registering new user: {}", request.getUsername());
        return userService.registerUser(request);
    }

}
