package com.example.cloudbees.demo.service.impl;

import com.example.cloudbees.demo.entity.Seat;
import com.example.cloudbees.demo.entity.Train;
import com.example.cloudbees.demo.model.TrainRequest;
import com.example.cloudbees.demo.repository.SeatRepository;
import com.example.cloudbees.demo.repository.TrainRepository;
import com.example.cloudbees.demo.service.TrainService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TrainServiceImpl implements TrainService {

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private SeatRepository seatRepository;

    public Train createTrain(TrainRequest trainRequest) throws Exception {

        Train dbTrain = trainRepository.findByTrainNumber(trainRequest.getTrainNumber()).orElse(null);
        if (dbTrain !=  null) {
            throw new Exception("Train  exist");
        }

        Train train = new Train();
        BeanUtils.copyProperties(trainRequest, train);
        train =  trainRepository.save(train);
        // Manually copy the seats
        if (trainRequest.getSeats() != null) {
            List<Seat> seatSet = new ArrayList<>();
            for (com.example.cloudbees.demo.model.Seat seat : trainRequest.getSeats()) {
                Seat newSeat = new Seat();
                BeanUtils.copyProperties(seat, newSeat);
                newSeat.setTrainId(train.getId());
                seatSet.add(newSeat);
            }
            seatSet = seatRepository.saveAll(seatSet);
            train.setSeats(seatSet);
        }
        return train;
    }

    public Train getTrainById(Long id) throws Exception {
        Train dbTrain = trainRepository.findById(id).orElse(null);
        if (dbTrain !=  null) {
            dbTrain.setSeats(seatRepository.findAllByTrainId(dbTrain.getId()));
        }
        return dbTrain;
    }

    public Optional<Train> getTrainByNumber(String trainNumber) {
        return trainRepository.findByTrainNumber(trainNumber);
    }

    public List<Train> getAllTrains() {
        List<Train> trains = trainRepository.findAll();
        for (Train dbTrain :  trains) {
            dbTrain.setSeats(seatRepository.findAllByTrainId(dbTrain.getId()));
        }
        return trains;
    }

    public Train updateTrain(Long id, TrainRequest updatedTrain) {
        Train existingTrain = trainRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Train not found"));
        BeanUtils.copyProperties(updatedTrain, existingTrain);
        // Manually copy the seats
        if (updatedTrain.getSeats() != null) {
            List<Seat> seats = new ArrayList<>();
            for (com.example.cloudbees.demo.model.Seat seat : updatedTrain.getSeats()) {
                Seat newSeat = new Seat();
                BeanUtils.copyProperties(seat, newSeat);
                newSeat.setTrainId(existingTrain.getId());
                seats.add(newSeat);
            }
            seats = seatRepository.saveAll(seats);
            existingTrain.setSeats(seats);
        }
        return trainRepository.save(existingTrain);
    }

    public void deleteTrain(Long id) {
        trainRepository.deleteById(id);
    }
}
