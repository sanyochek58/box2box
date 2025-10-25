package com.example.box2box_serv.box_serv.service;

import com.example.box2box_serv.auth_serv.entity.model.User;
import com.example.box2box_serv.auth_serv.events.UserRegisteredEvent;
import com.example.box2box_serv.auth_serv.exception.UserNotFoundException;
import com.example.box2box_serv.auth_serv.repository.UserRepository;
import com.example.box2box_serv.box_serv.entity.dto.CreateStorageDTO;
import com.example.box2box_serv.box_serv.entity.model.Storage;
import com.example.box2box_serv.box_serv.events.CreatedStorageEvent;
import com.example.box2box_serv.box_serv.exception.NotFoundStorageException;
import com.example.box2box_serv.box_serv.exception.NotFoundUserStoragesException;
import com.example.box2box_serv.box_serv.repository.StorageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.ast.tree.expression.Over;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService{
    private final StorageRepository storageRepository;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, Object> kafka;
    private final Logger log = LoggerFactory.getLogger(StorageServiceImpl.class);
    private static final String CREATED_STORAGE_TOPIC = "created-storage";

    @Override
    @Transactional
    @KafkaListener(topics = "user-registration", groupId = "box2box-group")
    public void WelcomeCreateStorage(UserRegisteredEvent event) throws UserNotFoundException{
        UUID user_id = event.user_id();
        String email = event.email();
        String username = event.username();

        User user = userRepository.findById(user_id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден !"));

        Storage storage = new Storage();
        storage.setName("welcome-storage by " + username);
        storage.setUser(user);
        storageRepository.save(storage);

        CreatedStorageEvent event_st = new CreatedStorageEvent(storage.getName(), username);
        kafka.send(CREATED_STORAGE_TOPIC, event_st);
        log.info("Создано хранилище " + storage.getName() + "для пользователя " + username);
    }

    @Override
    public List<CreateStorageDTO> getAllUserStorages(UUID uuid) throws NotFoundUserStoragesException, UserNotFoundException{
        User user = userRepository.findById(uuid).orElseThrow(() -> new UserNotFoundException("Пользователь не найден !"));
        List<Storage> storages = storageRepository.findByUser(user);
        if (storages.isEmpty()) {
            throw new NotFoundUserStoragesException("У пользователя нет хранилищ !");
        }
        return storages.stream()
                .map(storage -> new CreateStorageDTO(storage.getUuid(), storage.getName(), storage.getUser().getUuid()))
                .collect(Collectors.toList());
    }

    @Override
    public CreateStorageDTO findStorageByName(UUID user_id, String name) throws NotFoundStorageException, UserNotFoundException{
        User user = userRepository.findById(user_id).orElseThrow(() -> new UserNotFoundException("Пользователь не найден !"));
        Storage storage = storageRepository.findByUserAndName(user, name).orElseThrow(() -> new NotFoundStorageException("Хранилище не найдено !"));

        return new CreateStorageDTO(storage.getUuid(), storage.getName(), storage.getUser().getUuid());
    }
}
