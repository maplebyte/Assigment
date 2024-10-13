import com.task.thinkon.TestDataUtil;
import com.task.thinkon.dto.CreateUserDTO;
import com.task.thinkon.dto.UserDTO;
import com.task.thinkon.entities.User;
import com.task.thinkon.exceptions.EntityIsNullException;
import com.task.thinkon.exceptions.EntityNotFoundException;
import com.task.thinkon.exceptions.UniqueConstraintViolationException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        Long resultId = userService.createUser(createUserDTO);

        assertNotNull(resultId);
        assertEquals(1L, resultId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_NullInput() {
        assertThrows(EntityIsNullException.class, () -> {
            userService.createUser(null);
        });
    }

    @Test
    void testCreateUser_DuplicateFields() {
        CreateUserDTO createUserDTO = TestDataUtil.createUserDTO();
        createUserDTO.setEmail("duplicate@example.com");
        createUserDTO.setUsername("duplicate_user");
        createUserDTO.setPhoneNumber("+123456789");

        User existingUser = TestDataUtil.createUser();
        existingUser.setEmail("duplicate@example.com");
        existingUser.setUsername("duplicate_user");
        existingUser.setPhoneNumber("+123456789");

        when(userRepository.findByEmailOrUsernameOrPhoneNumberAndIdNot(
                createUserDTO.getEmail(),
                createUserDTO.getUsername(),
                createUserDTO.getPhoneNumber(),
                null
        )).thenReturn(List.of(existingUser));

        UniqueConstraintViolationException exception = assertThrows(
                UniqueConstraintViolationException.class,
                () -> userService.createUser(createUserDTO)
        );

        assertTrue(exception.getErrors().containsKey("email"));
        assertTrue(exception.getErrors().containsKey("username"));
        assertTrue(exception.getErrors().containsKey("phoneNumber"));

        verify(userRepository, times(1)).findByEmailOrUsernameOrPhoneNumberAndIdNot(
                createUserDTO.getEmail(),
                createUserDTO.getUsername(),
                createUserDTO.getPhoneNumber(),
                null
        );
        verify(userRepository, never()).save(any(User.class));
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
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_Success() {
        User user = TestDataUtil.createUser();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("john_doe", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
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
    void testUpdateUser_DuplicateFields() {
        CreateUserDTO updateUserDTO = TestDataUtil.createUserDTO();
        updateUserDTO.setEmail("duplicate@example.com");
        updateUserDTO.setUsername("duplicate_user");
        updateUserDTO.setPhoneNumber("+123456789");

        User existingUser = TestDataUtil.createUser();
        existingUser.setId(1L);

        User conflictingUser = TestDataUtil.createUser();
        conflictingUser.setId(2L);
        conflictingUser.setEmail("duplicate@example.com");
        conflictingUser.setUsername("duplicate_user");
        conflictingUser.setPhoneNumber("+123456789");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        when(userRepository.findByEmailOrUsernameOrPhoneNumberAndIdNot(
                updateUserDTO.getEmail(),
                updateUserDTO.getUsername(),
                updateUserDTO.getPhoneNumber(),
                1L
        )).thenReturn(List.of(conflictingUser));

        UniqueConstraintViolationException exception = assertThrows(
                UniqueConstraintViolationException.class,
                () -> userService.updateUser(1L, updateUserDTO)
        );

        assertTrue(exception.getErrors().containsKey("email"));
        assertTrue(exception.getErrors().containsKey("username"));
        assertTrue(exception.getErrors().containsKey("phoneNumber"));

        verify(userRepository, never()).save(any(User.class));

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByEmailOrUsernameOrPhoneNumberAndIdNot(
                updateUserDTO.getEmail(),
                updateUserDTO.getUsername(),
                updateUserDTO.getPhoneNumber(),
                1L
        );
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