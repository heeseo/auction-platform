package com.auction.auction.repository;

import com.auction.auction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // Custom query methods can be defined here if needed
    // For example, to find users by username:
    // Optional<User> findByUsername(String username);

    // Or to find users by email:
    // Optional<User> findByEmail(String email);
}
