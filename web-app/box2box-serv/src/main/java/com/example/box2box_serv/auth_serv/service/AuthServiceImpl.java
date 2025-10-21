package com.example.box2box_serv.auth_serv.service;

import com.example.box2box_serv.auth_serv.entity.dto.InfoDTO;
import com.example.box2box_serv.auth_serv.entity.dto.AuthResponseDTO;
import com.example.box2box_serv.auth_serv.entity.dto.LoginDTO;
import com.example.box2box_serv.auth_serv.entity.dto.RegistrationDTO;
import com.example.box2box_serv.auth_serv.entity.model.User;
import com.example.box2box_serv.auth_serv.exception.AlreadyUserExistsException;
import com.example.box2box_serv.auth_serv.exception.InvalidCredentialsException;
import com.example.box2box_serv.auth_serv.exception.UserNotFoundException;
import com.example.box2box_serv.auth_serv.repository.UserRepository;
import com.example.box2box_serv.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository repository;
    private final KafkaTemplate<String, Object> kafka;
    private final PasswordEncoder encoder;
    private final JwtService service;
    private static final String USER_REGISTRATION_TOPIC = "user-registration";

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    @Transactional
    public void register(RegistrationDTO dto)throws AlreadyUserExistsException {
        if (repository.findByEmail(dto.email()).isPresent()) {
            throw new AlreadyUserExistsException("Пользователь с таким email уже существует!");
        }

        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(encoder.encode(dto.password()));
        repository.save(user);

        kafka.send(USER_REGISTRATION_TOPIC, "Пользователь " + dto.email() + " успешно зарегистрирован!");
        log.info("Пользователь успешно зарегистрирован: {}", dto.email());
    }

    @Override
    public AuthResponseDTO login(LoginDTO dto) throws InvalidCredentialsException, UserNotFoundException {
        log.info("Попытка входа: {}", dto.email());
        User user = repository.findByEmail(dto.email())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден!"));

        if (!encoder.matches(dto.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Неверные данные!");
        }

        String token = service.generateToken(user.getEmail());
        log.info("Успешный вход: {}", user.getEmail());

        return new AuthResponseDTO(user.getEmail(), token);
    }

    @Override
    public InfoDTO getInfo(String email) throws Exception {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new Exception("Данные не найдены!"));
        return new InfoDTO(user.getUsername(), user.getEmail());
    }
}
