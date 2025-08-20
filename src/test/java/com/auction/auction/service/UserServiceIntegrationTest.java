package com.auction.auction.service;

import com.auction.auction.dto.UserRegistrationRequest;
import com.auction.auction.dto.UserResponse;
import com.auction.auction.model.User;
import com.auction.auction.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;


    @Test
    public void testUserRegistration() {

        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setEmail("test@example.com");
        request.setPassword("securepassword");
        request.setUsername("testuser");

        UserResponse userResponse = userService.registerUser(request);

        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getEmail()).isEqualTo("test@example.com");
        assertThat(userResponse.getUsername()).isEqualTo("testuser");
        assertThat(userRepository.existsByEmail("test@example.com")).isTrue();
        assertThat(userRepository.findById(userResponse.getId())).isPresent();
        assertThat(
                passwordEncoder.matches("securepassword",
                        userRepository.findById(userResponse.getId()).get().getPassword())
        ).isTrue();
        assertThat(userRepository.findById(userResponse.getId()).get().getUsername())
                .isEqualTo("testuser");
        assertThat(userRepository.findById(userResponse.getId()).get().getEmail())
                .isEqualTo("test@example.com");
        assertThat(userRepository.findById(userResponse.getId()).get().getBids()).isEmpty();

    }

    @Test
    public void testDuplicateEmailRegistration() {
        UserRegistrationRequest request1 = new UserRegistrationRequest();
        request1.setEmail("test1@example.com");
        request1.setPassword("securepassword");
        request1.setUsername("testuser1");
        userService.registerUser(request1);

        UserRegistrationRequest request2 = new UserRegistrationRequest();
        request2.setEmail("test1@example.com");
        request2.setPassword("securepassword");
        request2.setUsername("testuser2");

        assertThatThrownBy(() -> userService.registerUser(request2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already exists");

        // Verify that the first user is still registered
        User user = userRepository.findByEmail("test1@example.com")
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: test1@example.com"));
        assertThat(user.getEmail()).isEqualTo("test1@example.com");
        assertThat(user.getUsername()).isEqualTo("testuser1");
        assertThat(user.getBids()).isEmpty(); // Ensure no bids are associated initially
        assertThat(userRepository.count()).isEqualTo(1); // Only one user should be
        assertThat(userRepository.existsByEmail("test1@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("test2@example.com")).isFalse();

    }

}
