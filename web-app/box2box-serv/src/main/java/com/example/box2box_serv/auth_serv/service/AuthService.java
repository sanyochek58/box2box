package com.example.box2box_serv.auth_serv.service;

import com.example.box2box_serv.auth_serv.entity.dto.InfoDTO;
import com.example.box2box_serv.auth_serv.entity.dto.AuthResponseDTO;
import com.example.box2box_serv.auth_serv.entity.dto.LoginDTO;
import com.example.box2box_serv.auth_serv.entity.dto.RegistrationDTO;
import com.example.box2box_serv.auth_serv.exception.AlreadyUserExistsException;
import com.example.box2box_serv.auth_serv.exception.InvalidCredentialsException;
import com.example.box2box_serv.auth_serv.exception.UserNotFoundException;

public interface AuthService {
    AuthResponseDTO login(LoginDTO dto) throws InvalidCredentialsException, UserNotFoundException;
    void register(RegistrationDTO dto)throws AlreadyUserExistsException;
    InfoDTO getInfo(String email) throws Exception;
}
