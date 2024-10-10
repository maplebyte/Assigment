package com.task.thinkon;

import com.task.thinkon.dto.CreateUserDTO;
import com.task.thinkon.entities.User;

public class TestDataUtil {

    public static User createUser() {
        User existingUser = new User();
        existingUser.setId(1L);
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
