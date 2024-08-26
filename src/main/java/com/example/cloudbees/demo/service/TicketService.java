package com.example.cloudbees.demo.service;

import com.example.cloudbees.demo.entity.Ticket;
import com.example.cloudbees.demo.model.PurchaseRequest;
import com.example.cloudbees.demo.model.PurchaseResponse;
import com.example.cloudbees.demo.model.Section;

import java.util.List;
import java.util.Optional;

public interface TicketService {

    public PurchaseResponse purchaseTicket(PurchaseRequest request) throws Exception;

    public List<PurchaseResponse> getTicketByUser(Long userId);

    public List<PurchaseResponse> getTicketsBySection(Long userId, Section section);

    public void removeUser(Long trainId, Long userId);

    public PurchaseResponse modifySeat(Long ticketId, Long newSeatNumber) throws Exception;

}
