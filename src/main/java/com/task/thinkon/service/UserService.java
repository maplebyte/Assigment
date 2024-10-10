package com.task.thinkon.service;

import com.task.thinkon.dto.CreateUserDTO;
import com.task.thinkon.dto.UserDTO;
import com.task.thinkon.dto.mapper.UserMapper;
import com.task.thinkon.entities.User;
import com.task.thinkon.exceptions.EntityNotFoundException;
import com.task.thinkon.exceptions.EntityIsNullException;
import com.task.thinkon.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public UserDTO createUser(CreateUserDTO createUserDTO) {
        if (Objects.isNull(createUserDTO)) {
            throw new EntityIsNullException();
        }
        User user = UserMapper.toEntity(createUserDTO);
        User savedUser = userRepository.save(user);

        log.info("User successfully created, ID: {} ", savedUser.getId());
        return UserMapper.toDTO(savedUser);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        return UserMapper.toDTO(user);
    }

    public UserDTO updateUser(Long id, CreateUserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
        User updatedUser = UserMapper.updateEntityFromDTO(userDTO, existingUser);
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
}
