package com.example.cloudbees.demo.controller;

import com.example.cloudbees.demo.entity.Train;
import com.example.cloudbees.demo.model.TrainRequest;
import com.example.cloudbees.demo.service.TrainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class TrainControllerTest {

    @Mock
    private TrainService trainService;

    @InjectMocks
    private TrainController trainController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTrain() throws Exception {
        TrainRequest trainRequest = new TrainRequest();
        Train train = new Train();
        when(trainService.createTrain(any(TrainRequest.class))).thenReturn(train);

        ResponseEntity<Train> result = trainController.createTrain(trainRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(train, result.getBody());
        verify(trainService, times(1)).createTrain(trainRequest);
    }

    @Test
    public void testGetTrainById() throws Exception {
        Train train = new Train();
        when(trainService.getTrainById(anyLong())).thenReturn(train);

        ResponseEntity<Train> result = trainController.getTrainById(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(train, result.getBody());
        verify(trainService, times(1)).getTrainById(1L);
    }

    @Test
    public void testGetAllTrains() {
        List<Train> trains = Arrays.asList(new Train(), new Train());
        when(trainService.getAllTrains()).thenReturn(trains);

        ResponseEntity<List<Train>> result = trainController.getAllTrains();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(trains, result.getBody());
        verify(trainService, times(1)).getAllTrains();
    }

    @Test
    public void testUpdateTrain() {
        TrainRequest trainRequest = new TrainRequest();
        Train train = new Train();
        when(trainService.updateTrain(anyLong(), any(TrainRequest.class))).thenReturn(train);

        ResponseEntity<String> result = trainController.updateTrain(1L, trainRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("updated successfully", result.getBody());
        verify(trainService, times(1)).updateTrain(1L, trainRequest);
    }

    @Test
    public void testDeleteTrain() {
        doNothing().when(trainService).deleteTrain(anyLong());

        ResponseEntity<Void> result = trainController.deleteTrain(1L);

        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(trainService, times(1)).deleteTrain(1L);
    }
}
