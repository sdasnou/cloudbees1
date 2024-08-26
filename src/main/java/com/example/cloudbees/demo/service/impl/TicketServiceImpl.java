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
import com.example.cloudbees.demo.service.TicketService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private SeatRepository seatRepository;

    public PurchaseResponse purchaseTicket(PurchaseRequest request) throws Exception {
        Train train = null;
        Seat seat = null;
        User dbUser = userRepository.findByEmail(request.getUser().getEmail()).orElse(null);
        if (dbUser ==  null) {
            dbUser = userRepository.save(request.getUser());
        }
        List<Train> trains = (List<Train>) trainRepository.findAllAvailableTrain(request.getFromLocation(), request.getToLocation());
        if(trains == null){
            throw  new Exception("No Train Found");
        }
        Ticket ticket = new Ticket();
        for(Train trainDB : trains) {
            for( Seat seatDB: seatRepository.findAllByTrainId(trainDB.getId())) {
                if (seatDB.isAvailable()) {
                    train = trainDB;
                    seat = seatDB;
                    break;
                }
            }
        }
        if (train == null){
            throw  new Exception("No Train / Seat Available");
        }
        BeanUtils.copyProperties(request, ticket);
        ticket.setUser(dbUser);
        ticket.setTrain(train);
        ticket.setSeatId(seat.getId());

        ticket = ticketRepository.save(ticket);
        seat.setAvailable(false);
        seat.setUserId(dbUser.getId());
        seat = seatRepository.save(seat);
        return prepareResponse(ticket, train, dbUser, Arrays.asList(seat));

    }

    private PurchaseResponse prepareResponse(Ticket ticket, Train train, User dbUser, List<Seat> seats) {
        PurchaseResponse purchaseResponse = new PurchaseResponse();
        BeanUtils.copyProperties(ticket, purchaseResponse);
        com.example.cloudbees.demo.model.Train train1 = new com.example.cloudbees.demo.model.Train();
        BeanUtils.copyProperties(train,train1);
        List<com.example.cloudbees.demo.model.Seat> seatList = new ArrayList<>();
        for (Seat seat : seats) {
            com.example.cloudbees.demo.model.Seat seat1 = new com.example.cloudbees.demo.model.Seat();
            BeanUtils.copyProperties(seat, seat1);
            seatList.add(seat1);
        }
        train1.setSeats(seatList);
        purchaseResponse.setTrain(train1);
        return purchaseResponse;
    }

    public List<PurchaseResponse> getTicketByUser(Long userId) {
        List<PurchaseResponse> responses = new ArrayList<>();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Ticket> tickets = ticketRepository.findByUser(user);

        for(Ticket ticket: tickets){
            PurchaseResponse purchaseResponse = new PurchaseResponse();
            BeanUtils.copyProperties(ticket, purchaseResponse);
            responses.add(prepareResponse(ticket, ticket.getTrain(), ticket.getUser(), seatRepository.getByUserId(ticket.getTrain().getId(), userId)));
        }
        return responses;
    }

    public List<PurchaseResponse> getTicketsBySection(Long userId, Section section) {
        List<PurchaseResponse> responses = new ArrayList<>();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Ticket> tickets =  ticketRepository.findByUser(user);

        for(Ticket ticket: tickets){
            PurchaseResponse purchaseResponse = new PurchaseResponse();
            BeanUtils.copyProperties(ticket, purchaseResponse);
            List<Seat> seats = seatRepository.getByUserId(ticket.getTrain().getId(), userId);
            for (Seat seat: seats) {
                if (seat.getSection().equals(section.toString())) {
                    responses.add(prepareResponse(ticket, ticket.getTrain(), ticket.getUser(), Arrays.asList(seat)));
                }
            }
        }
        return responses;
    }

    public void removeUser(Long trainId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Ticket> tickets = ticketRepository.findByUser(user);
        for(Ticket ticket: tickets){
            if (ticket.getTrain().getId() == trainId){
                ticketRepository.delete(ticket);
            }
        }
        userRepository.delete(user);
    }

    public PurchaseResponse modifySeat(Long ticketId, Long newSeatNumber) throws Exception {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        Train train = (Train) trainRepository.findById(ticket.getTrain().getId())
                .orElseThrow(() -> new RuntimeException("Train not found"));
        Seat seat = null;
        for( Seat seatDB: seatRepository.findAllByTrainId(train.getId())) {
            if (seatDB.getSeatNumber() == newSeatNumber &&  seatDB.isAvailable()) {
                seat = seatDB;
                break;
            }
        }

        if (seat == null){
            throw  new Exception("No Train / Seat Available");
        }
        ticket.setSeatId(newSeatNumber);
        ticket = ticketRepository.save(ticket);
        seat.setAvailable(false);
        seat = seatRepository.save(seat);

        PurchaseResponse purchaseResponse = new PurchaseResponse();
        BeanUtils.copyProperties(ticket, purchaseResponse);
        return prepareResponse(ticket, ticket.getTrain(), ticket.getUser(), seatRepository.getByUserId(ticket.getTrain().getId(), ticket.getUser().getId()));
    }
}
