package com.task.thinkon;

import com.task.thinkon.dto.CreateUserDTO;
import com.task.thinkon.entities.User;

import java.util.UUID;

public class TestDataUtil {

    public static final UUID FIXED_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    public static User createUser() {
        User existingUser = new User();
        existingUser.setId(FIXED_UUID);
        existingUser.setUsername("john_doe");
        existingUser.setFirstName("John");
        existingUser.setLastName("Doe");
        existingUser.setEmail("john.doe@example.com");
        existingUser.setPhoneNumber("+123456789");
        return existingUser;
    }

    public static CreateUserDTO createUserDTO() {
        CreateUserDTO updateUserDTO = new CreateUserDTO();
        updateUserDTO.setUsername("john_doe");
        updateUserDTO.setFirstName("Johnny");
        updateUserDTO.setLastName("Doe");
        updateUserDTO.setEmail("johnny.doe@example.com");
        updateUserDTO.setPhoneNumber("+123456789");
        return updateUserDTO;
    }
}
