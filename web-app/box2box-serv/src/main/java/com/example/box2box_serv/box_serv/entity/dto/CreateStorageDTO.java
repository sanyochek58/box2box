package com.example.box2box_serv.box_serv.entity.dto;

import com.example.box2box_serv.auth_serv.entity.model.User;

import java.util.UUID;

public record CreateStorageDTO(UUID uuid,String name, UUID user_id) {}
