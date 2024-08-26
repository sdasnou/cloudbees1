package com.example.cloudbees.demo.service.impl;

import com.example.cloudbees.demo.entity.Seat;
import com.example.cloudbees.demo.entity.Train;
import com.example.cloudbees.demo.model.TrainRequest;
import com.example.cloudbees.demo.repository.SeatRepository;
import com.example.cloudbees.demo.repository.TrainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TrainServiceImplTest {

    @Mock
    private TrainRepository trainRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private TrainServiceImpl trainService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTrain_Success() throws Exception {
        TrainRequest trainRequest = new TrainRequest();
        trainRequest.setTrainNumber("12345");
        trainRequest.setSeats(new ArrayList<>());

        when(trainRepository.findByTrainNumber(anyString())).thenReturn(Optional.empty());
        when(trainRepository.save(any(Train.class))).thenReturn(new Train());
        when(seatRepository.saveAll(anyList())).thenReturn(new ArrayList<>());

        Train createdTrain = trainService.createTrain(trainRequest);

        assertNotNull(createdTrain);
        verify(trainRepository, times(1)).save(any(Train.class));
        verify(seatRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void testCreateTrain_TrainAlreadyExists() {
        TrainRequest trainRequest = new TrainRequest();
        trainRequest.setTrainNumber("12345");

        when(trainRepository.findByTrainNumber(anyString())).thenReturn(Optional.of(new Train()));

        Exception exception = assertThrows(Exception.class, () -> {
            trainService.createTrain(trainRequest);
        });

        assertEquals("Train  exist", exception.getMessage());
    }

    @Test
    public void testGetTrainById_Success() throws Exception {
        Train train = new Train();
        train.setId(1L);

        when(trainRepository.findById(anyLong())).thenReturn(Optional.of(train));
        when(seatRepository.findAllByTrainId(anyLong())).thenReturn(new ArrayList<>());

        Train foundTrain = trainService.getTrainById(1L);

        assertNotNull(foundTrain);
        verify(trainRepository, times(1)).findById(anyLong());
        verify(seatRepository, times(1)).findAllByTrainId(anyLong());
    }

    @Test
    public void testGetTrainById_TrainNotFound() throws Exception {
        when(trainRepository.findById(anyLong())).thenReturn(Optional.empty());

        Train foundTrain = trainService.getTrainById(1L);

        assertNull(foundTrain);
    }

    @Test
    public void testGetTrainByNumber_Success() {
        Train train = new Train();
        when(trainRepository.findByTrainNumber(anyString())).thenReturn(Optional.of(train));

        Optional<Train> foundTrain = trainService.getTrainByNumber("12345");

        assertTrue(foundTrain.isPresent());
        verify(trainRepository, times(1)).findByTrainNumber(anyString());
    }

    @Test
    public void testGetAllTrains_Success() {
        Train train1 = new Train();
        train1.setId(1L);
        Train train2 = new Train();
        train2.setId(2L);

        List<Train> trains = Arrays.asList(train1, train2);

        Seat seat1 = new Seat();
        seat1.setTrainId(1L);
        Seat seat2 = new Seat();
        seat2.setTrainId(2L);

        when(trainRepository.findAll()).thenReturn(trains);
        when(seatRepository.findAllByTrainId(1L)).thenReturn(Arrays.asList(seat1));
        when(seatRepository.findAllByTrainId(2L)).thenReturn(Arrays.asList(seat2));

        List<Train> result = trainService.getAllTrains();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        assertEquals(1, result.get(0).getSeats().size());
        assertEquals(1, result.get(1).getSeats().size());

        verify(trainRepository, times(1)).findAll();
        verify(seatRepository, times(1)).findAllByTrainId(1L);
        verify(seatRepository, times(1)).findAllByTrainId(2L);
        verify(seatRepository, never()).saveAll(anyList());
    }

    @Test
    public void testUpdateTrain_TrainNotFound() {
        TrainRequest trainRequest = new TrainRequest();

        when(trainRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            trainService.updateTrain(1L, trainRequest);
        });

        assertEquals("Train not found", exception.getMessage());
    }

    @Test
    public void testDeleteTrain_Success() {
        doNothing().when(trainRepository).deleteById(anyLong());

        trainService.deleteTrain(1L);

        verify(trainRepository, times(1)).deleteById(anyLong());
    }
}

