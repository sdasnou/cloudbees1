package com.example.cloudbees.demo.service;

import com.example.cloudbees.demo.entity.User;
import java.util.Optional;

public interface UserService {

    public User addUser(User user) throws Exception;
    public Optional<User> getUserById(Long id);
    public User updateUser(Long id, User updatedUser) throws Exception;
    public void deleteUserById(Long id);
}
