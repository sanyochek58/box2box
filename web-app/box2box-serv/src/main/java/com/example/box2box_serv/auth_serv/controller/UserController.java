package com.example.box2box_serv.auth_serv.controller;

import com.example.box2box_serv.auth_serv.entity.dto.AuthResponseDTO;
import com.example.box2box_serv.auth_serv.entity.dto.InfoDTO;
import com.example.box2box_serv.auth_serv.entity.dto.LoginDTO;
import com.example.box2box_serv.auth_serv.entity.dto.RegistrationDTO;
import com.example.box2box_serv.auth_serv.exception.AlreadyUserExistsException;
import com.example.box2box_serv.auth_serv.exception.InvalidCredentialsException;
import com.example.box2box_serv.auth_serv.exception.UserNotFoundException;
import com.example.box2box_serv.auth_serv.service.AuthServiceImpl;
import com.example.box2box_serv.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/box2box/auth")
public class UserController {
    private final AuthServiceImpl service;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationDTO dto) throws AlreadyUserExistsException {
        service.register(dto);
        return ResponseEntity.ok("Пользователь успешно зарегистрирован !");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO dto) throws InvalidCredentialsException, UserNotFoundException {
        AuthResponseDTO response = service.login(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<InfoDTO> getMe(@RequestHeader("Authorization") String header) throws Exception {
        if(header == null || !header.startsWith("Bearer ")){
            throw new Exception("Отстутствует токен авторизации");
        }

        String token = header.substring(7);
        String email = jwtService.extractEmail(token);

        InfoDTO dto = service.getInfo(email);
        return ResponseEntity.ok(dto);
    }
}
