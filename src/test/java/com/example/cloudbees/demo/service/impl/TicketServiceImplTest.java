package com.example.cloudbees.demo.service.impl;

import com.example.cloudbees.demo.entity.Seat;
import com.example.cloudbees.demo.entity.Ticket;
import com.example.cloudbees.demo.entity.Train;
import com.example.cloudbees.demo.entity.User;
import com.example.cloudbees.demo.model.PurchaseRequest;
import com.example.cloudbees.demo.model.PurchaseResponse;
import com.example.cloudbees.demo.model.Section;
import com.example.cloudbees.demo.repository.SeatRepository;
import com.example.cloudbees.demo.repository.TicketRepository;
import com.example.cloudbees.demo.repository.TrainRepository;
import com.example.cloudbees.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class TicketServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrainRepository trainRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPurchaseTicket_Success() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        Train train = new Train();
        train.setId(1L);

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setAvailable(true);
        seat.setTrainId(train.getId());

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setSeatId(seat.getId());
        ticket.setTrain(train);
        ticket.setUser(user);

        PurchaseRequest request = new PurchaseRequest();
        request.setUser(user);
        request.setFromLocation("Location A");
        request.setToLocation("Location B");

        // Mock repository interactions
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        when(trainRepository.findAllAvailableTrain(request.getFromLocation(), request.getToLocation()))
                .thenReturn(Collections.singletonList(train));
        when(seatRepository.findAllByTrainId(train.getId())).thenReturn(Collections.singletonList(seat));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(seatRepository.save(any(Seat.class))).thenReturn(seat);

        // Execute the method under test
        PurchaseResponse response = ticketService.purchaseTicket(request);

        // Verify interactions
        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, times(1)).save(user);
        verify(trainRepository, times(1)).findAllAvailableTrain(request.getFromLocation(), request.getToLocation());
        verify(seatRepository, times(1)).findAllByTrainId(train.getId());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
        verify(seatRepository, times(1)).save(any(Seat.class));

        // Verify the response
        assertEquals(train.getTrainName(), response.getTrain().getTrainName());
    }

    @Test
    public void testPurchaseTicket_NoTrainFound() {
        PurchaseRequest request = new PurchaseRequest();
        request.setUser(new User());

        when(trainRepository.findAllAvailableTrain(anyString(), anyString())).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {
            ticketService.purchaseTicket(request);
        });

        assertEquals("No Train / Seat Available", exception.getMessage());
    }

    @Test
    public void testPurchaseTicket_NoSeatAvailable() {
        Train train = new Train();
        when(trainRepository.findAllAvailableTrain(anyString(), anyString())).thenReturn(Arrays.asList(train));
        when(seatRepository.findAllByTrainId(anyLong())).thenReturn(Arrays.asList());

        PurchaseRequest request = new PurchaseRequest();
        request.setUser(new User());

        Exception exception = assertThrows(Exception.class, () -> {
            ticketService.purchaseTicket(request);
        });

        assertEquals("No Train / Seat Available", exception.getMessage());
    }

    @Test
    public void testGetTicketByUser_Success() {
        User user = new User();
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setTrain(new Train());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(ticketRepository.findByUser(any(User.class))).thenReturn(Arrays.asList(ticket));
        when(seatRepository.getByUserId(anyLong(), anyLong())).thenReturn(Arrays.asList(new Seat()));

        List<PurchaseResponse> responses = ticketService.getTicketByUser(1L);

        assertFalse(responses.isEmpty());
        verify(ticketRepository, times(1)).findByUser(any(User.class));
    }

    @Test
    public void testGetTicketsBySection_Success() {
        Long userId = 1L;
        Section section = Section.SECTION_A;

        User user = new User();
        user.setId(userId);

        Train train = new Train();
        train.setId(1L);

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setUser(user);
        ticket.setTrain(train);

        Seat seat = new Seat();
        seat.setTrainId(train.getId());
        seat.setSection(section.toString());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ticketRepository.findByUser(user)).thenReturn(Arrays.asList(ticket));
        when(seatRepository.getByUserId(train.getId(), userId)).thenReturn(Arrays.asList(seat));

        List<PurchaseResponse> responses = ticketService.getTicketsBySection(userId, section);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(section.toString(), responses.get(0).getTrain().getSeats().get(0).getSection());

        verify(userRepository, times(1)).findById(userId);
        verify(ticketRepository, times(1)).findByUser(user);
        verify(seatRepository, times(1)).getByUserId(train.getId(), userId);
    }

    @Test
    public void testRemoveUser_Success() {
        Long trainId = 1L;
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTrain(new Train());
        ticket.getTrain().setId(trainId);

        // Mocking UserRepository
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Mocking TicketRepository
        when(ticketRepository.findByUser(user)).thenReturn(Collections.singletonList(ticket));

        // Execute the method under test
        ticketService.removeUser(trainId, userId);

        // Verify interactions
        verify(ticketRepository, times(1)).delete(ticket);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testModifySeat_NoSeatAvailable() {
        Long ticketId = 1L;
        Long newSeatNumber = 2L;

        Ticket ticket = new Ticket();
        ticket.setId(ticketId);

        Train train = new Train();
        train.setId(1L);

        ticket.setTrain(train);

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(trainRepository.findById(train.getId())).thenReturn(Optional.of(train));
        when(seatRepository.findAllByTrainId(train.getId())).thenReturn(java.util.Collections.emptyList());

        try {
            ticketService.modifySeat(ticketId, newSeatNumber);
            fail("Expected Exception not thrown");
        } catch (Exception e) {
            assertEquals("No Train / Seat Available", e.getMessage());
        }
    }
}
