package com.example.cloudbees.demo.repository;

import com.example.cloudbees.demo.entity.Train;
import com.example.cloudbees.demo.entity.User;
import com.example.cloudbees.demo.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cloudbees.demo.entity.Ticket;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query(value = "select e from Ticket e where e.user =:user", nativeQuery = false)
    List<Ticket> findByUser(User user);
    void deleteByUser(User user);

}
