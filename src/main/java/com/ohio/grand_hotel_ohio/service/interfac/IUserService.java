package com.ohio.grand_hotel_ohio.service.interfac;

import com.ohio.grand_hotel_ohio.dto.LoginRequest;
import com.ohio.grand_hotel_ohio.dto.Response;
import com.ohio.grand_hotel_ohio.entity.Users;

public interface IUserService {

    Response register(Users loginRequest);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);
}
