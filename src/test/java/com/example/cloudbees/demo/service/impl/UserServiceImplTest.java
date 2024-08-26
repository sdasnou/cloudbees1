package com.example.cloudbees.demo.service.impl;

import com.example.cloudbees.demo.entity.User;
import com.example.cloudbees.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddUser_Success() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.addUser(user);

        assertNotNull(createdUser);
        assertEquals("test@example.com", createdUser.getEmail());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testAddUser_UserAlreadyExists() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(Exception.class, () -> {
            userService.addUser(user);
        });

        assertEquals("user exist", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(anyString());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void testGetUserById_Success() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals(1L, foundUser.get().getId());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testGetUserById_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.getUserById(1L);

        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    public void testUpdateUser_Success() throws Exception {
        User existingUser = new User();
        existingUser.setId(1L);

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail("updated@example.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(1L, updatedUser);

        assertNotNull(result);
        assertEquals("updated@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        User updatedUser = new User();
        updatedUser.setId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(1L, updatedUser);
        });

        assertEquals("Train not found", exception.getMessage());
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void testDeleteUserById_Success() {
        doNothing().when(userRepository).deleteById(anyLong());

        userService.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(anyLong());
    }
}

