package com.ohio.grand_hotel_ohio.service.impl;

import com.ohio.grand_hotel_ohio.domain.Roles;
import com.ohio.grand_hotel_ohio.dto.LoginRequest;
import com.ohio.grand_hotel_ohio.dto.Response;
import com.ohio.grand_hotel_ohio.dto.UserDTO;
import com.ohio.grand_hotel_ohio.entity.Users;
import com.ohio.grand_hotel_ohio.exception.OurException;
import com.ohio.grand_hotel_ohio.repo.UserRepository;
import com.ohio.grand_hotel_ohio.service.interfac.IUserService;
import com.ohio.grand_hotel_ohio.service.utils.JWTUtils;
import com.ohio.grand_hotel_ohio.service.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public Response register(Users users) {
        Response response = new Response();
        try{
            if(users.getRole() == null){
                users.setRole(Roles.USER);

            }
            if(userRepository.existsByEmail(users.getEmail())){
                throw new OurException(users.getEmail() + "Already Exists");
            }
            users.setPassword(passwordEncoder.encode(users.getPassword()));
            Users savedUsers = userRepository.save(users);
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUsers);
            response.setStatus(200);
            response.setUsers(userDTO);


        }catch (OurException e){
            response.setStatus(400);
            response.setMessage(e.getMessage());

        }
        catch (Exception e){
            response.setStatus(500);
            response.setMessage("Error Occured During Suring User Registration" +  e.getMessage());

        }
        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {

        Response response = new Response();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));

            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()->new OurException("user Not found"));

            var token = jwtUtils.generateToken(user);
            response.setStatus(200);
            response.setToken(token);
            response.setRole(user.getRole().name());
            response.setExpirationTime("7 Days");
            response.setMessage("succesful");
        }catch (OurException e){
            response.setStatus(404);
            response.setMessage(e.getMessage());

        }catch (Exception e){
            response.setStatus(500);
            response.setMessage("Error Occured During User Login" +  e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response = new Response();

        try {
            List<Users>usersList = userRepository.findAll();
            List<UserDTO>userDTOList = Utils.mapUserListEntityToUserListDTO(usersList);
            response.setStatus(200);
            response.setMessage("succesful");
            response.setUserList(userDTOList);
        }
        catch (Exception e){
            response.setStatus(500);
            response.setMessage("Error getting all users" +  e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserBookingHistory(String userId) {
        Response response = new Response();


        try{
            Users users = userRepository.findById(Long.valueOf(userId)).orElseThrow(()->new OurException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusUserBookingsAndRooms(users);

            response.setStatus(200);
            response.setMessage("succesful");
            response.setUsers(userDTO);
        }catch (OurException e){

            response.setStatus(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatus(500);
            response.setMessage("Error getting all users" +  e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUser(String userId) {
        Response response = new Response();


        try{
            userRepository.findById(Long.valueOf(userId)).orElseThrow(()->new OurException("User Not Found"));
            userRepository.deleteById(Long.valueOf(userId));

            response.setStatus(200);
            response.setMessage("succesful");
        }catch (OurException e){

            response.setStatus(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatus(500);
            response.setMessage("Error getting all users" +  e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserById(String userId) {
        Response response = new Response();


        try{
            Users users = userRepository.findById(Long.valueOf(userId)).orElseThrow(()->new OurException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(users);

            response.setStatus(200);
            response.setMessage("succesful");
            response.setUsers(userDTO);

        }catch (OurException e){

            response.setStatus(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatus(500);
            response.setMessage("Error getting all users" +  e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        Response response = new Response();


        try{
            Users users = userRepository.findByEmail(email).orElseThrow(()->new OurException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(users);

            response.setStatus(200);
            response.setMessage("succesful");
            response.setUsers(userDTO);

        }catch (OurException e){

            response.setStatus(404);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatus(500);
            response.setMessage("Error getting all users" +  e.getMessage());
        }
        return response;
    }
}
