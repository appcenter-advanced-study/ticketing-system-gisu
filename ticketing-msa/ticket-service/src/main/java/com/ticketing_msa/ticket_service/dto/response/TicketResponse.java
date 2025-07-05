package com.ticketing_msa.ticket_service.dto.response;

import com.ticketing_msa.ticket_service.domain.Ticket;

public record TicketResponse(Long id, String name) {

    public static TicketResponse from(Ticket ticket) {
        return new TicketResponse(ticket.getId(), ticket.getName());
    }
}
