package com.example.cloudbees.demo.controller;

import com.example.cloudbees.demo.model.PurchaseRequest;
import com.example.cloudbees.demo.model.PurchaseResponse;
import com.example.cloudbees.demo.model.Section;
import com.example.cloudbees.demo.service.TicketService;
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

public class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPurchaseTicket() throws Exception {
        PurchaseRequest request = new PurchaseRequest();
        PurchaseResponse response = new PurchaseResponse();
        when(ticketService.purchaseTicket(any(PurchaseRequest.class))).thenReturn(response);

        ResponseEntity<PurchaseResponse> result = ticketController.purchaseTicket(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(ticketService, times(1)).purchaseTicket(request);
    }

    @Test
    public void testGetReceipt() {
        List<PurchaseResponse> responses = Arrays.asList(new PurchaseResponse(), new PurchaseResponse());
        when(ticketService.getTicketByUser(anyLong())).thenReturn(responses);

        ResponseEntity<List<PurchaseResponse>> result = ticketController.getReceipt(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responses, result.getBody());
        verify(ticketService, times(1)).getTicketByUser(1L);
    }

    @Test
    public void testGetUsersBySectionAndTrain() {
        List<PurchaseResponse> responses = Arrays.asList(new PurchaseResponse(), new PurchaseResponse());
        when(ticketService.getTicketsBySection(anyLong(), any(Section.class))).thenReturn(responses);

        ResponseEntity<List<PurchaseResponse>> result = ticketController.getUsersBySectionAndTrain(1L, Section.SECTION_A);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(responses, result.getBody());
        verify(ticketService, times(1)).getTicketsBySection(1L, Section.SECTION_A);
    }

    @Test
    public void testRemoveUser() {
        doNothing().when(ticketService).removeUser(anyLong(), anyLong());

        ResponseEntity<String> result = ticketController.removeUser(1L, 2L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("user removed successfully", result.getBody());
        verify(ticketService, times(1)).removeUser(1L, 2L);
    }

    @Test
    public void testModifySeat() throws Exception {
        PurchaseResponse response = new PurchaseResponse();
        when(ticketService.modifySeat(anyLong(), anyLong())).thenReturn(response);

        ResponseEntity<PurchaseResponse> result = ticketController.modifySeat(1L, 10L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(ticketService, times(1)).modifySeat(1L, 10L);
    }
}
