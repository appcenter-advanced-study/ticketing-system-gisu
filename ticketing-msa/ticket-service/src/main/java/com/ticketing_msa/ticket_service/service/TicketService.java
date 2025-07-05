package com.ticketing_msa.ticket_service.service;

import com.ticketing_msa.ticket_service.domain.Ticket;
import com.ticketing_msa.ticket_service.dto.request.CreateTicketRequest;
import com.ticketing_msa.ticket_service.dto.response.TicketResponse;
import com.ticketing_msa.ticket_service.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public void createTicket(CreateTicketRequest request) {
        Ticket ticket = new Ticket(request.name());
        ticketRepository.save(ticket);
    }

    public TicketResponse findById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("티켓을 찾을 수 없습니다."));
        return TicketResponse.from(ticket);
    }

    public List<TicketResponse> findAll() {
        return ticketRepository.findAll()
                .stream()
                .map(TicketResponse::from)
                .toList();
    }

    public void deleteById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("삭제할 티켓이 없습니다. ID=" + id));
        ticketRepository.delete(ticket);
    }
}
