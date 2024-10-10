import com.task.thinkon.TestDataUtil;
import com.task.thinkon.dto.CreateUserDTO;
import com.task.thinkon.dto.UserDTO;
import com.task.thinkon.entities.User;
import com.task.thinkon.exceptions.EntityIsNullException;
import com.task.thinkon.exceptions.EntityNotFoundException;
import com.task.thinkon.repository.UserRepository;
import com.task.thinkon.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateUser_Success() {
        CreateUserDTO createUserDTO = TestDataUtil.createUserDTO();
        User user = TestDataUtil.createUser();

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.createUser(createUserDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("john_doe", result.getUsername());
        verify(userRepository, times(1)).save(any(User.class));  // Проверяем, что save вызван один раз
    }

    @Test
    void testCreateUser_NullInput() {
        assertThrows(EntityIsNullException.class, () -> {
            userService.createUser(null);
        });
    }

    @Test
    void testGetAllUsers_Success() {
        List<User> users = new ArrayList<>();
        User user = TestDataUtil.createUser();
        users.add(user);

        when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("john_doe", result.get(0).getUsername());
        verify(userRepository, times(1)).findAll();  // Проверяем, что findAll вызван один раз
    }

    @Test
    void testGetUserById_Success() {
        User user = TestDataUtil.createUser();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("john_doe", result.getUsername());
        verify(userRepository, times(1)).findById(1L);  // Проверяем, что findById вызван один раз
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserById(1L);
        });
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateUser_Success() {
        User existingUser = TestDataUtil.createUser();
        CreateUserDTO updateUserDTO = TestDataUtil.createUserDTO();

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserDTO result = userService.updateUser(1L, updateUserDTO);

        assertNotNull(result);
        assertEquals("Johnny", result.getFirstName());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteUser(1L);
        });
        verify(userRepository, times(1)).existsById(1L);
    }
}