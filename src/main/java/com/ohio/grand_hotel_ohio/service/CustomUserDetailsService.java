package com.ohio.grand_hotel_ohio.service;

import com.ohio.grand_hotel_ohio.exception.OurException;
import com.ohio.grand_hotel_ohio.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService{


    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       return (UserDetails) userRepository.findByEmail(username).orElseThrow(()-> new OurException("A felhasználónév/e-mail cím nem található"));
    }
}
