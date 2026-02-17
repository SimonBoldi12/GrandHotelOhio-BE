package com.ohio.grand_hotel_ohio.controller;

import com.ohio.grand_hotel_ohio.dto.LoginRequest;
import com.ohio.grand_hotel_ohio.dto.Response;
import com.ohio.grand_hotel_ohio.entity.Users;
import com.ohio.grand_hotel_ohio.service.interfac.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {


    private final IUserService userService;

    public AuthController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody Users users){
        Response response = userService.register(users);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest){
        Response response = userService.login(loginRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


}
