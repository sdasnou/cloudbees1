package com.example.cloudbees.demo.controller;

import com.example.cloudbees.demo.entity.Train;
import com.example.cloudbees.demo.model.TrainRequest;
import com.example.cloudbees.demo.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trains")
public class TrainController {

    @Autowired
    private TrainService trainService;

    @PostMapping
    public ResponseEntity<Train> createTrain(@RequestBody TrainRequest trainRequest) throws Exception {
        Train createdTrain = trainService.createTrain(trainRequest);
        return ResponseEntity.ok(createdTrain);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Train> getTrainById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(trainService.getTrainById(id));
    }

    @GetMapping
    public ResponseEntity<List<Train>> getAllTrains() {
        List<Train> trains = trainService.getAllTrains();
        return ResponseEntity.ok(trains);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTrain(@PathVariable Long id, @RequestBody TrainRequest trainRequest) {
        Train updatedTrain = trainService.updateTrain(id, trainRequest);
        return ResponseEntity.ok("updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrain(@PathVariable Long id) {
        trainService.deleteTrain(id);
        return ResponseEntity.noContent().build();
    }
}
