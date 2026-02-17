package com.ohio.grand_hotel_ohio.entity;

import com.ohio.grand_hotel_ohio.domain.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;


@Entity
@Table(name = "users")
@AllArgsConstructor
@Getter
@Setter
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @NotBlank(message = "First name is required")
    @Column(name="firstName")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(name="lastName")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Column(name="email")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Column(name="phoneNumber")
    private String phoneNumber;

    @Column(name="password")
    @NotBlank(message = "Password is required!")
    private String password;


    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Roles role;

    @OneToMany(mappedBy = "users",  fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();


    //constructor

    public Users(String firstName, String lastName, String email, String phoneNumber, String password, Roles role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
    }

    public Users(){

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.asAuthority()));
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}