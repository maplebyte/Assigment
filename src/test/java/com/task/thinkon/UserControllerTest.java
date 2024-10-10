package com.task.thinkon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.thinkon.dto.CreateUserDTO;
import com.task.thinkon.dto.UserDTO;
import com.task.thinkon.exceptions.EntityNotFoundException;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        UserDTO createdUser = new UserDTO();
        createdUser.setId(1L);
        createdUser.setUsername("john_doe");

        Mockito.when(userService.createUser(Mockito.any(CreateUserDTO.class))).thenReturn(createdUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("john_doe"));
    }

    @Test
    void testGetAllUsers_Success() throws Exception {
        List<UserDTO> userList = new ArrayList<>();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("john_doe");
        userList.add(userDTO);

        Mockito.when(userService.getAllUsers()).thenReturn(userList);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("john_doe"));
    }

    @Test
    void testGetUserById_Success() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("john_doe");

        Mockito.when(userService.getUserById(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("john_doe"));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        Mockito.when(userService.getUserById(1L)).thenThrow(new EntityNotFoundException(1L));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        CreateUserDTO updateUserDTO = TestDataUtil.createUserDTO();

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(1L);
        updatedUserDTO.setUsername("john_doe");

        Mockito.when(userService.updateUser(Mockito.eq(1L), Mockito.any(CreateUserDTO.class))).thenReturn(updatedUserDTO);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("john_doe"));
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        Mockito.doThrow(new EntityNotFoundException(1L)).when(userService).deleteUser(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNotFound());
    }
}
