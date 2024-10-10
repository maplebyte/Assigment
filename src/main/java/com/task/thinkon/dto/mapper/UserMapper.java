package com.task.thinkon.dto.mapper;

import com.task.thinkon.dto.CreateUserDTO;
import com.task.thinkon.dto.UserDTO;
import com.task.thinkon.entities.User;
import com.task.thinkon.exceptions.EntityIsNullException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class UserMapper {

    private UserMapper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static User toEntity(CreateUserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());

        return user;
    }

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());

        return dto;
    }

    public static User updateEntityFromDTO(CreateUserDTO dto, User user) {
        if (dto == null || user == null) {
            log.error("Attempted to update entity from DTO, DTO and User must not be null");
            throw new EntityIsNullException();
        }

        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());

        return user;
    }
}
