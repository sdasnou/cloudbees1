package com.example.cloudbees.demo.service;

import com.example.cloudbees.demo.entity.Train;
import com.example.cloudbees.demo.model.TrainRequest;

import java.util.List;
import java.util.Optional;

public interface TrainService {
    public Train createTrain(TrainRequest trainRequest) throws Exception;
    public Train getTrainById(Long id) throws Exception;
    public Optional<Train> getTrainByNumber(String trainNumber);
    public List<Train> getAllTrains();
    public Train updateTrain(Long id,  TrainRequest updatedTrainRequest);
    public void deleteTrain(Long id);

}
