package com.auction.auction.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserTest {

    @Test
    void testUserCreation() {
        User user = User.builder()
                .email("test@example.com")
                .password("securepassword")
                .username("testuser")
                .build();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isNull(); // ID should be null before persisting
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPassword()).isEqualTo("securepassword");
        assertThat(user.getUsername()).isEqualTo("testuser");
        assertThat(user.getBids()).isEmpty(); // Ensure no bids are associated initially

    }
}