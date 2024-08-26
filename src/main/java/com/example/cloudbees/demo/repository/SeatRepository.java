package com.example.cloudbees.demo.repository;


import com.example.cloudbees.demo.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    @Query(value = "select e from Seat e where e.trainId=:trainId", nativeQuery = false)
    List<Seat> findAllByTrainId(Long trainId);

    @Query(value = "select e from Seat e where e.trainId=:trainId and e.userId=:userId", nativeQuery = false)
    List<Seat> getByUserId(Long trainId, Long userId);
}
