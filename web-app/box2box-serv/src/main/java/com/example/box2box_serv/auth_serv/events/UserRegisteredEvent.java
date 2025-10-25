package com.example.box2box_serv.auth_serv.events;

import java.util.UUID;

public record UserRegisteredEvent(UUID user_id, String email, String username) {}
