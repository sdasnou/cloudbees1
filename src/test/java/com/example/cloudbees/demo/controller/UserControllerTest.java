package com.example.cloudbees.demo.controller;

import com.example.cloudbees.demo.entity.User;
import com.example.cloudbees.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddUser() throws Exception {
        User user = new User();
        when(userService.addUser(any(User.class))).thenReturn(user);

        ResponseEntity<User> result = userController.addUser(user);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(user, result.getBody());
        verify(userService, times(1)).addUser(user);
    }

    @Test
    public void testGetUserById_Found() {
        User user = new User();
        when(userService.getUserById(anyLong())).thenReturn(Optional.of(user));

        ResponseEntity<User> result = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(user, result.getBody());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    public void testGetUserById_NotFound() {
        when(userService.getUserById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<User> result = userController.getUserById(1L);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = new User();
        when(userService.updateUser(anyLong(), any(User.class))).thenReturn(user);

        ResponseEntity<User> result = userController.updateUser(1L, user);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(user, result.getBody());
        verify(userService, times(1)).updateUser(1L, user);
    }

    @Test
    public void testDeleteUserById() {
        doNothing().when(userService).deleteUserById(anyLong());

        ResponseEntity<Void> result = userController.deleteUserById(1L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(userService, times(1)).deleteUserById(1L);

    }
}
