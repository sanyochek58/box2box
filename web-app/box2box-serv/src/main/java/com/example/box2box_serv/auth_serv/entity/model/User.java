package com.example.box2box_serv.auth_serv.entity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Entity
@Table(name="users")
@Getter
@Setter
public class User {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private UUID uuid;

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private String password;

}
