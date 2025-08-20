package com.auction.auction.service;

import com.auction.auction.dto.UserRegistrationRequest;
import com.auction.auction.dto.UserResponse;
import com.auction.auction.model.User;
import com.auction.auction.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testRegisterUser() {

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            ReflectionTestUtils.setField(user, "id", 1L);

            return user;
        });

        when(userRepository.existsByEmail(any(String.class))).thenReturn(false);

        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        UserRegistrationRequest requestDto = new UserRegistrationRequest();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("securepassword");
        requestDto.setUsername("testuser");

        UserResponse userResponse = userService.registerUser(requestDto);


        verify(userRepository).save(Mockito.any(User.class));
        verifyNoMoreInteractions(userRepository);
        verify(passwordEncoder).encode("securepassword");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword");


        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getId()).isNotNull();
        assertThat(userResponse.getId()).isEqualTo(1L);
        assertThat(userResponse.getEmail()).isEqualTo("test@example.com");
        assertThat(userResponse.getUsername()).isEqualTo("testuser");

    }

    @Test
    void testRegisterUserWithDuplicateEmail() {

        when(userRepository.existsByEmail(any(String.class))).thenReturn(true);

        UserRegistrationRequest requestDto = new UserRegistrationRequest();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("securepassword");
        requestDto.setUsername("testuser");


        assertThatThrownBy(() -> userService.registerUser(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already exists");

        verify(userRepository).existsByEmail("test@example.com");
        verify(userRepository, never()).save(any(User.class));

        verify(passwordEncoder, never()).encode(any(String.class));

    }

}