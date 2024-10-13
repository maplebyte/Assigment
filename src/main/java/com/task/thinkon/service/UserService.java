package com.task.thinkon.service;

import com.task.thinkon.dto.CreateUserDTO;
import com.task.thinkon.dto.UserDTO;
import com.task.thinkon.dto.mapper.UserMapper;
import com.task.thinkon.entities.User;
import com.task.thinkon.exceptions.EntityIsNullException;
import com.task.thinkon.exceptions.EntityNotFoundException;
import com.task.thinkon.exceptions.UniqueConstraintViolationException;
import com.task.thinkon.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long createUser(CreateUserDTO createUserDTO) {
        if (Objects.isNull(createUserDTO)) {
            log.error("Provided entity is null");
            throw new EntityIsNullException();
        }

        Map<String, String> validationErrors = validateUniqueConstraints(createUserDTO, null);

        if (!validationErrors.isEmpty()) {
            throw new UniqueConstraintViolationException(validationErrors);
        }

        User user = UserMapper.toEntity(createUserDTO);
        User savedUser = userRepository.save(user);
        long savedUserId = savedUser.getId();

        log.info("User successfully created, ID: {} ", savedUserId);
        return savedUserId;
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User with ID: {} not found", id);
                    return new EntityNotFoundException(id);
                });
        return UserMapper.toDTO(user);
    }

    public UserDTO updateUser(Long id, CreateUserDTO createUserDTO) {
        if (Objects.isNull(createUserDTO)) {
            log.error("Provided entity is null");
            throw new EntityIsNullException();
        }

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User with ID: {} not found", id);
                    return new EntityNotFoundException(id);
                });

        Map<String, String> validationErrors = validateUniqueConstraints(createUserDTO, id);

        if (!validationErrors.isEmpty()) {
            throw new UniqueConstraintViolationException(validationErrors);
        }

        User updatedUser = UserMapper.updateEntityFromDTO(createUserDTO, existingUser);
        userRepository.save(updatedUser);

        log.info("User with ID: {} successfully updated", id);
        return UserMapper.toDTO(updatedUser);
    }

    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("User with ID: {} successfully deleted", id);
        } else {
            throw new EntityNotFoundException(id);
        }
    }

    private Map<String, String> validateUniqueConstraints(CreateUserDTO createUserDTO, Long userId) {
        List<User> conflictingUsers = userRepository.findByEmailOrUsernameOrPhoneNumberAndIdNot(
                createUserDTO.getEmail(),
                createUserDTO.getUsername(),
                createUserDTO.getPhoneNumber(),
                userId);

        Map<String, String> validationErrors = new HashMap<>();

        for (User existingUser : conflictingUsers) {
            if (!existingUser.getId().equals(userId) && existingUser.getEmail().equals(createUserDTO.getEmail())) {
                log.error("Email {} is already in use", createUserDTO.getEmail());
                validationErrors.put("email", "Email is already in use");
            }

            if (!existingUser.getId().equals(userId) && existingUser.getUsername().equals(createUserDTO.getUsername())) {
                log.error("Username {} is already in use", createUserDTO.getUsername());
                validationErrors.put("username", "Username is already in use");
            }

            if (!existingUser.getId().equals(userId) && existingUser.getPhoneNumber().equals(createUserDTO.getPhoneNumber())) {
                log.error("Phone number {} is already in use", createUserDTO.getPhoneNumber());
                validationErrors.put("phoneNumber", "Phone number is already in use");
            }
        }

        return validationErrors;
    }
}