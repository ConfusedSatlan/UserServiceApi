package org.userservice.common.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.userservice.common.model.entity.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private static UserRepository userRepository;
    private static User testUser;
    private static final Long EXISTING_USER_ID = 1L;
    private static final Long NON_EXISTING_USER_ID = 1000L;

    @BeforeAll
    static void setUp() {
        userRepository = new UserRepository();
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
    }

    @BeforeEach
    void cleanStorage() {
        userRepository.clean();
    }

    @Test
    void addUser_ValidUser_ReturnsAddedUser() {
        User addedUser = userRepository.addUser(testUser);
        assertNotNull(addedUser.getId());
        assertEquals(testUser.getEmail(), addedUser.getEmail());
        assertEquals(testUser.getFirstName(), addedUser.getFirstName());
        assertEquals(testUser.getLastName(), addedUser.getLastName());
    }

    @Test
    void getUser_ExistingUserId_ReturnsUser() {
        userRepository.addUser(testUser);
        User retrievedUser = userRepository.getUser(EXISTING_USER_ID);
        assertNotNull(retrievedUser);
        assertEquals(testUser, retrievedUser);
    }

    @Test
    void getUser_NonExistingUserId_ReturnsNull() {
        User retrievedUser = userRepository.getUser(NON_EXISTING_USER_ID);
        assertNull(retrievedUser);
    }

    @Test
    void getAllUsers_ReturnsAllNonDeletedUsers() {
        userRepository.addUser(testUser);
        User user2 = new User();
        user2.setEmail("test2@example.com");
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        userRepository.addUser(user2);
        userRepository.deleteUser(user2.getId());
        List<User> allUsers = userRepository.getAllUsers();
        assertEquals(1, userRepository.getAllUsers().size());
    }

    @Test
    void deleteUser_ExistingUserId_DeletesUser() {
        userRepository.addUser(testUser);
        boolean isDeleted = userRepository.deleteUser(EXISTING_USER_ID);
        assertTrue(isDeleted);
        assertNull(userRepository.getUser(EXISTING_USER_ID));
    }

    @Test
    void deleteUser_NonExistingUserId_ReturnsFalse() {
        boolean isDeleted = userRepository.deleteUser(NON_EXISTING_USER_ID);
        assertFalse(isDeleted);
    }

    @Test
    void updateUser_ExistingUserId_UpdatesUser() {
        userRepository.addUser(testUser);
        User updatedUser = new User();
        updatedUser.setId(EXISTING_USER_ID);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setFirstName("Jane");
        updatedUser.setLastName("Doe");
        User returnedUser = userRepository.updateUser(EXISTING_USER_ID, updatedUser);
        assertNotNull(returnedUser);
        assertEquals(updatedUser.getEmail(), returnedUser.getEmail());
        assertEquals(updatedUser.getFirstName(), returnedUser.getFirstName());
        assertEquals(updatedUser.getLastName(), returnedUser.getLastName());
    }

    @Test
    void updateUser_NonExistingUserId_ReturnsNull() {
        User updatedUser = new User();
        updatedUser.setId(NON_EXISTING_USER_ID);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setFirstName("Jane");
        updatedUser.setLastName("Doe");
        User returnedUser = userRepository.updateUser(NON_EXISTING_USER_ID, updatedUser);
        assertNull(returnedUser);
    }
}
