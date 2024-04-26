package org.userservice.common.service;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.userservice.common.model.dto.CreateRequestUserDto;
import org.userservice.common.model.dto.UserDto;
import org.userservice.common.model.entity.User;
import org.userservice.common.model.exception.UserServiceApiException;
import org.userservice.common.repository.UserRepository;
import org.userservice.common.service.impl.UserServiceImpl;
import org.userservice.common.service.mapper.UserMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private static Long USER_ID;
    private static CreateRequestUserDto requestUserDto;
    private static User createdUser;
    private static UserDto createdUserDto;
    private static final LocalDate birthDateOne = LocalDate.of(1990, 1, 1);

    @BeforeAll
    static void setUp() {

        USER_ID = 1L;
        requestUserDto = new CreateRequestUserDto(
                "test@example.com",
                "John",
                "Doe",
                birthDateOne,
                "123 Main St",
                "1234567890"
        );
        createdUser = new User();
        createdUserDto = new UserDto();
    }

    @Test
    void createUser_ValidUser_ReturnsCreatedUser() {
        when(userMapper.mapToModel(requestUserDto)).thenReturn(createdUser);
        when(userRepository.addUser(createdUser)).thenReturn(createdUser);
        when(userMapper.mapToDto(createdUser)).thenReturn(createdUserDto);

        UserDto result = userService.createUser(requestUserDto);

        assertNotNull(result);
        assertEquals(createdUserDto, result);
    }

    @Test
    void getUserById_ValidId_ReturnsUser() {
        when(userRepository.getUser(USER_ID)).thenReturn(createdUser);
        when(userMapper.mapToDto(createdUser)).thenReturn(createdUserDto);

        UserDto result = userService.getUserById(USER_ID);

        assertNotNull(result);
        assertEquals(createdUserDto, result);
    }

    @Test
    void getUserById_InvalidId_ThrowsException() {
        when(userRepository.getUser(USER_ID)).thenReturn(null);

        assertThrows(UserServiceApiException.class, () -> userService.getUserById(USER_ID));
    }

    @Test
    void updateUser_ValidData_ReturnsUpdatedUser() {
        UserDto updatedUserDto = new UserDto();
        when(userRepository.getUser(USER_ID)).thenReturn(createdUser);
        when(userRepository.updateUser(USER_ID, createdUser)).thenReturn(createdUser);
        when(userMapper.mapToDto(createdUser)).thenReturn(updatedUserDto);

        UserDto result = userService.updateUser(USER_ID, createdUserDto);

        assertNotNull(result);
        assertEquals(updatedUserDto, result);
    }

    @Test
    void updateUser_InvalidId_ThrowsException() {
        when(userRepository.getUser(USER_ID)).thenReturn(null);

        assertThrows(UserServiceApiException.class, () -> userService.updateUser(USER_ID, createdUserDto));
    }

    @Test
    void deleteUser_ValidId_ReturnsTrue() {
        when(userRepository.deleteUser(USER_ID)).thenReturn(true);

        boolean result = userService.deleteUser(USER_ID);

        assertTrue(result);
    }

    @Test
    void deleteUser_InvalidId_ThrowsException() {
        when(userRepository.deleteUser(USER_ID)).thenThrow(new UserServiceApiException("User doesn't exist with id: " + USER_ID,
                String.valueOf(HttpStatus.NOT_FOUND.value())));

        Exception exception = Assert.assertThrows(
                UserServiceApiException.class,
                () -> userService.deleteUser(USER_ID)
        );
        assertEquals("User doesn't exist with id: " + USER_ID, exception.getMessage());
    }

    @Test
    void searchUsersByBirthDateRange_ValidRange_ReturnsUsers() {
        LocalDate fromDate = LocalDate.of(1990, 1, 1);
        LocalDate toDate = LocalDate.of(2000, 1, 1);

        User user1 = new User();
        user1.setBirthDate(LocalDate.of(1995, 5, 5));
        User user2 = new User();
        user2.setBirthDate(LocalDate.of(1980, 10, 10));
        List<User> allUsers = Arrays.asList(user1, user2);

        UserDto userDto1 = new UserDto();
        userDto1.setBirthDate(user1.getBirthDate());
        List<UserDto> expectedUserDtoList = Arrays.asList(userDto1);

        when(userRepository.getAllUsers()).thenReturn(allUsers);
        when(userMapper.mapToDto(user1)).thenReturn(userDto1);

        List<UserDto> result = userService.searchUsersByBirthDateRange(fromDate, toDate);

        assertEquals(expectedUserDtoList, result);
    }

    @Test
    void searchUsersByBirthDateRange_InvalidRange_ReturnsEmptyList() {
        LocalDate fromDate = LocalDate.now().plusYears(20);
        LocalDate toDate = LocalDate.now().plusYears(30);

        List<UserDto> result = userService.searchUsersByBirthDateRange(fromDate, toDate);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
