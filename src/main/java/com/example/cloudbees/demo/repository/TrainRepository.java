package com.example.cloudbees.demo.repository;

import com.example.cloudbees.demo.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {
    Optional<Train> findByTrainNumber(String trainNumber);
    @Query(value = "select e from Train e where e.fromLocation =:fromLocation and e.toLocation=:toLocation", nativeQuery = false)
    List<Train> findAllAvailableTrain(String fromLocation, String toLocation);
}
