package com.ohio.grand_hotel_ohio.service.impl;

import com.ohio.grand_hotel_ohio.domain.Roles;
import com.ohio.grand_hotel_ohio.dto.LoginRequest;
import com.ohio.grand_hotel_ohio.dto.Response;
import com.ohio.grand_hotel_ohio.entity.Users;
import com.ohio.grand_hotel_ohio.repo.UserRepository;
import com.ohio.grand_hotel_ohio.service.utils.JWTUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    private Users testUser;

    @BeforeEach
    void setUp() {

        testUser = new Users();
        testUser.setId(1L);
        testUser.setFirstName("Proba");
        testUser.setLastName("Teszt");
        testUser.setEmail("proba@teszt.hu");
        testUser.setPhoneNumber("06208518120");
        testUser.setPassword("titkosjelszo");
        testUser.setRole(Roles.USER);
    }

    @Test
    void testRegister_Success() {

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("titkositott_hash");
        when(userRepository.save(any(Users.class))).thenReturn(testUser);


        Response response = userService.register(testUser);


        assertEquals(200, response.getStatus());
        assertNotNull(response.getUsers());
        assertEquals("proba@teszt.hu", response.getUsers().getEmail());


        verify(userRepository, times(1)).save(any(Users.class));
    }

    @Test
    void testRegister_EmailAlreadyExists() {

        when(userRepository.existsByEmail(anyString())).thenReturn(true);


        Response response = userService.register(testUser);


        assertEquals(400, response.getStatus());
        assertTrue(response.getMessage().contains("Már létezik"));


        verify(userRepository, never()).save(any(Users.class));
    }

    @Test
    void testLogin_Success() {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("proba@teszt.hu");
        loginRequest.setPassword("titkosjelszo");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(jwtUtils.generateToken(any(Users.class))).thenReturn("fake-jwt-token");


        Response response = userService.login(loginRequest);


        assertEquals(200, response.getStatus());
        assertEquals("fake-jwt-token", response.getToken());
        assertEquals("USER", response.getRole());
        assertEquals("sikeres", response.getMessage());


        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testLogin_UserNotFound() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("nemletezo@teszt.hu");
        loginRequest.setPassword("titkosjelszo");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Response response = userService.login(loginRequest);

        assertEquals(404, response.getStatus());
        assertEquals("A felhasználó nem található", response.getMessage());
    }
}