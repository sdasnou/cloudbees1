package com.example.cloudbees.demo.controller;

import com.example.cloudbees.demo.model.PurchaseRequest;
import com.example.cloudbees.demo.model.PurchaseResponse;
import com.example.cloudbees.demo.model.Section;
import com.example.cloudbees.demo.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

    @Autowired
    private TicketService ticketService;

    @PostMapping("/purchase")
    public ResponseEntity<PurchaseResponse> purchaseTicket(@RequestBody PurchaseRequest request) throws Exception {
        logger.info("purchaseTicket request {}", request);
        return ResponseEntity.ok(ticketService.purchaseTicket(request));
    }

    @GetMapping("/receipt/{userId}")
    public ResponseEntity<List<PurchaseResponse>> getReceipt(@PathVariable Long userId) {
        logger.info("getReceipt userId {}", userId);
        return ResponseEntity.ok(ticketService.getTicketByUser(userId));
    }

    @GetMapping("/user/{userId}/section/{section}")
    public ResponseEntity<List<PurchaseResponse>> getUsersBySectionAndTrain(@PathVariable Long userId, @PathVariable Section section) {
        logger.info("getUsersBySectionAndTrain user {} section {}", userId, section);
        return ResponseEntity.ok(ticketService.getTicketsBySection(userId, section));
    }

    @DeleteMapping("/train/{trainId}/user/{userId}")
    public ResponseEntity<String> removeUser(@PathVariable Long trainId, @PathVariable Long userId) {
        logger.info("getUsersBySectionAndTrain trainId {} userId {}", trainId, userId);
        ticketService.removeUser(trainId, userId);
        return ResponseEntity.ok("user removed successfully");
    }

    @PutMapping("/modifySeat/{ticketId}")
    public ResponseEntity<PurchaseResponse> modifySeat(@PathVariable Long ticketId, @RequestParam Long newSeatNumber) throws Exception {
        logger.info("getUsersBySectionAndTrain ticketId {} newSeatNumber {}", ticketId, newSeatNumber);
        return ResponseEntity.ok(ticketService.modifySeat(ticketId, newSeatNumber));
    }
}

