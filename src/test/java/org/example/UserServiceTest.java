package org.example;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userService = new UserService();
        user = new User("JohnDoe", "password", "johndoe@example.com");
    }

    @Test
    void testRegisterUser_Success() {
        boolean result = userService.registerUser(user);
        assertTrue(result, "User should be registered successfully.");
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        userService.registerUser(user);
        boolean result = userService.registerUser(user);
        assertFalse(result, "User registration should fail if the username already exists.");
    }

    @Test
    void testRegisterUser_EmptyUsername() {
        User emptyUsernameUser = new User("", "password", "emptyuser@example.com");
        boolean result = userService.registerUser(emptyUsernameUser);
        assertFalse(result, "User registration should fail if the username is empty.");
    }

    @Test
    void testLoginUser_Success() {
        userService.registerUser(user);
        User loggedInUser = userService.loginUser("JohnDoe", "password");
        assertNotNull(loggedInUser, "Login should succeed with correct username and password.");
    }

    @Test
    void testLoginUser_WrongPassword() {
        userService.registerUser(user);
        User loggedInUser = userService.loginUser("JohnDoe", "wrongpassword");
        assertNull(loggedInUser, "Login should fail if the password is incorrect.");
    }

    @Test
    void testLoginUser_UserDoesNotExist() {
        User loggedInUser = userService.loginUser("NonExistentUser", "password");
        assertNull(loggedInUser, "Login should fail if the username does not exist.");
    }
    @Test
    void testUpdateUserProfile_Success() {
        boolean result = userService.updateUserProfile(user, "NewJohnDoe", "newpassword", "newjohndoe@example.com");
        assertTrue(result, "User profile should be updated successfully.");
        assertEquals("NewJohnDoe", user.getUsername(), "Username should be updated.");
        assertEquals("newpassword", user.getPassword(), "Password should be updated.");
        assertEquals("newjohndoe@example.com", user.getEmail(), "Email should be updated.");
    }

    @Test
    void testUpdateUserProfile_UsernameAlreadyTaken() {
        User anotherUser = new User("JaneDoe", "password123", "janedoe@example.com");
        userService.registerUser(anotherUser);
        boolean result = userService.updateUserProfile(user, "JaneDoe", "newpassword", "newjohndoe@example.com");
        assertFalse(result, "User profile update should fail if the new username is already taken.");
    }

    @Test
    void testUpdateUserProfile_EmptyUsername() {
        boolean result = userService.updateUserProfile(user, "", "newpassword", "newjohndoe@example.com");
        assertFalse(result, "User profile update should fail if the new username is empty.");
    }
}
