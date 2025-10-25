package com.example.box2box_serv.box_serv.repository;

import com.example.box2box_serv.auth_serv.entity.model.User;
import com.example.box2box_serv.box_serv.entity.model.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StorageRepository extends JpaRepository<Storage, UUID> {
    List<Storage> findByUser(User user);
    Optional<Storage> findByUserAndName(User user, String name);
}
