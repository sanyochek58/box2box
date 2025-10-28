package com.example.box2box_serv.box_serv.service;

import com.example.box2box_serv.auth_serv.events.UserRegisteredEvent;
import com.example.box2box_serv.auth_serv.exception.UserNotFoundException;
import com.example.box2box_serv.box_serv.entity.dto.CreateStorageDTO;
import com.example.box2box_serv.box_serv.exception.NotFoundStorageException;
import com.example.box2box_serv.box_serv.exception.NotFoundUserStoragesException;

import java.util.List;
import java.util.UUID;

public interface StorageService {
    void WelcomeCreateStorage(UserRegisteredEvent event) throws UserNotFoundException;
    List<CreateStorageDTO> getAllUserStorages(UUID uuid) throws NotFoundUserStoragesException, UserNotFoundException;
    CreateStorageDTO findStorageByName(UUID user_id, String name) throws NotFoundStorageException, UserNotFoundException;
    CreateStorageDTO createStorage(UUID userID, String storageName);
}
