package com.auction.auction.controller;

import com.auction.auction.dto.UserLoginRequest;
import com.auction.auction.dto.UserRegistrationRequest;
import com.auction.auction.dto.UserResponse;
import com.auction.auction.model.User;
import com.auction.auction.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/users/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getUsername(), user.getEmail()));
    }

}
