package com.example.cloudbees.demo.repository;

import com.example.cloudbees.demo.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cloudbees.demo.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}