package com.example.box2box_serv.box_serv.controller;

import com.example.box2box_serv.auth_serv.entity.model.User;
import com.example.box2box_serv.auth_serv.exception.UserNotFoundException;
import com.example.box2box_serv.auth_serv.repository.UserRepository;
import com.example.box2box_serv.box_serv.entity.dto.CreateStorageDTO;
import com.example.box2box_serv.box_serv.service.StorageServiceImpl;
import com.example.box2box_serv.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/box2box/storages")
public class StorageController {
    private final StorageServiceImpl service;
    private final JwtService jwtService;
    private final UserRepository repository;

    @GetMapping("/my-storages")
    public ResponseEntity<List<CreateStorageDTO>> myStorage(@RequestHeader("Authorization") String header) throws Exception {
        if(header == null || !header.startsWith("Bearer ")){
            throw new Exception("Отсутствует токен авторизации !");
        }

        String token = header.substring(7);
        String email = jwtService.extractEmail(token);

        User user = repository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Пользователь не найден !"));

        List<CreateStorageDTO> storages = service.getAllUserStorages(user.getUuid());
        return ResponseEntity.ok(storages);
    }

    @GetMapping("/storage/{name}")
    public ResponseEntity<CreateStorageDTO> storageByName(@RequestHeader("Authorization") String header, String name)throws Exception{
        if(header == null || !header.startsWith("Bearer ")){
            throw new Exception("Отсутствует токен авторизации !");
        }

        String token = header.substring(7);
        String email = jwtService.extractEmail(token);

        User user = repository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Пользователь не найден !"));

        CreateStorageDTO storage = service.findStorageByName(user.getUuid(), name);
        return ResponseEntity.ok(storage);
    }
}
