package com.task.thinkon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.thinkon.dto.CreateUserDTO;
import com.task.thinkon.dto.UserDTO;
import com.task.thinkon.exceptions.EntityNotFoundException;
import com.task.thinkon.exceptions.UniqueConstraintViolationException;
import com.task.thinkon.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateUser_Success() throws Exception {
        CreateUserDTO createUserDTO = TestDataUtil.createUserDTO();
        UUID createdUserId = TestDataUtil.FIXED_UUID;

        Mockito.when(userService.createUser(Mockito.any(CreateUserDTO.class))).thenReturn(createdUserId);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("User created successfully"))
                .andExpect(jsonPath("$.data").value(1L));
    }

    @Test
    void testCreateUser_EmailAlreadyExists() throws Exception {
        CreateUserDTO createUserDTO = TestDataUtil.createUserDTO();

        Mockito.when(userService.createUser(Mockito.any(CreateUserDTO.class)))
                .thenThrow(new UniqueConstraintViolationException(Map.of("email", "Email is already in use")));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Conflict: Unique constraint violations: [email: Email is already in use] "));
    }

    @Test
    void testGetAllUsers_Success() throws Exception {
        List<UserDTO> userList = new ArrayList<>();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(TestDataUtil.FIXED_UUID);
        userDTO.setUsername("john_doe");
        userList.add(userDTO);

        Mockito.when(userService.getAllUsers()).thenReturn(userList);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("Users retrieved successfully"))
                .andExpect(jsonPath("$.data[0].id").value(TestDataUtil.FIXED_UUID))
                .andExpect(jsonPath("$.data[0].username").value("john_doe"));
    }


    @Test
    void testGetUserById_Success() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(TestDataUtil.FIXED_UUID);
        userDTO.setUsername("john_doe");

        Mockito.when(userService.getUserById(TestDataUtil.FIXED_UUID)).thenReturn(userDTO);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("User retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.username").value("john_doe"));
    }


    @Test
    void testGetUserById_NotFound() throws Exception {
        Mockito.when(userService.getUserById(TestDataUtil.FIXED_UUID)).thenThrow(new EntityNotFoundException(TestDataUtil.FIXED_UUID));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Entity not found: User with id 1 not found"));
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        CreateUserDTO updateUserDTO = TestDataUtil.createUserDTO();

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(TestDataUtil.FIXED_UUID);
        updatedUserDTO.setUsername("john_doe");

        Mockito.when(userService.updateUser(Mockito.eq(TestDataUtil.FIXED_UUID), Mockito.any(CreateUserDTO.class))).thenReturn(updatedUserDTO);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("User updated successfully"))
                .andExpect(jsonPath("$.data.id").value(TestDataUtil.FIXED_UUID))
                .andExpect(jsonPath("$.data.username").value("john_doe"));
    }

    @Test
    void testUpdateUser_EmailAlreadyExists() throws Exception {
        CreateUserDTO updateUserDTO = TestDataUtil.createUserDTO();

        Mockito.when(userService.updateUser(Mockito.eq(TestDataUtil.FIXED_UUID), Mockito.any(CreateUserDTO.class)))
                .thenThrow(new UniqueConstraintViolationException(Map.of("email", "Email is already in use")));

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Conflict: Unique constraint violations: [email: Email is already in use] "));
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(TestDataUtil.FIXED_UUID);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        Mockito.doThrow(new EntityNotFoundException(TestDataUtil.FIXED_UUID)).when(userService).deleteUser(TestDataUtil.FIXED_UUID);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Entity not found: User with id 1 not found"));
    }

    @Test
    void testCreateUser_ValidationErrors() throws Exception {
        CreateUserDTO invalidUserDTO = new CreateUserDTO();
        invalidUserDTO.setUsername("");
        invalidUserDTO.setFirstName("John");
        invalidUserDTO.setLastName("Doe");
        invalidUserDTO.setEmail("invalid-email");
        invalidUserDTO.setPhoneNumber("+123456789");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.data.username").value("Username is mandatory"))
                .andExpect(jsonPath("$.data.email").value("Email should be valid"));
    }
}