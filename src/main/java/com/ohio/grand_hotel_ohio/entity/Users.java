package com.ohio.grand_hotel_ohio.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")

public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="first_name")
    private String firstName;


    @Column(name="last_name")
    private String lastName;


    @Column(name="email")
    private String email;

    @Column(name="phoneNumber")
    private String phoneNumber;

    @Column(name="password")
    private String password;

    @Column(name="role")
    private String role;


    //constructor


//    public Users(String firstName, String lastName, String email, String phoneNumber, String password, String role) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.phoneNumber = phoneNumber;
//        this.password = password;
//        this.role = role;
//    }

    public Users() {

    }


    //getter

    //setter
}
