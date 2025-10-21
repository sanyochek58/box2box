package com.example.box2box_serv.auth_serv.repository;

import com.example.box2box_serv.auth_serv.entity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
