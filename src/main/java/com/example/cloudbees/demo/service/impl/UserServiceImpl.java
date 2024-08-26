package com.example.cloudbees.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.cloudbees.demo.entity.User;
import com.example.cloudbees.demo.repository.UserRepository;
import com.example.cloudbees.demo.service.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User addUser(User user) throws Exception {
        User dbUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (dbUser !=  null) {
            throw new Exception("user exist");
        }
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, User updatedUser) throws Exception {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Train not found"));
        if (existingUser ==  null) {
            throw new Exception("user doesn't exist");
        }
        return userRepository.save(updatedUser);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
