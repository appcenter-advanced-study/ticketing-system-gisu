package com.ticketing_msa.ticket_service.controller;


import com.ticketing_msa.ticket_service.dto.request.CreateTicketRequest;
import com.ticketing_msa.ticket_service.dto.response.TicketResponse;
import com.ticketing_msa.ticket_service.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<Void> createTicket(@RequestBody CreateTicketRequest request) {
        ticketService.createTicket(request);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable Long ticketId) {
        TicketResponse result = ticketService.findById(ticketId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TicketResponse>> getAllTicket() {
        List<TicketResponse> result = ticketService.findAll();
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long ticketId) {
        ticketService.deleteById(ticketId);
        return ResponseEntity.ok().build();
    }

}
