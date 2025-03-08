package com.effservice.server.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.effservice.server.schema.NoteEntity;
import com.effservice.server.schema.NoteRepository;
import com.effservice.server.schema.TicketRepository;
import com.effservice.server.schema.TicketsEntity;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private amqpTicket rabbitMQProducer;

    public List<TicketsEntity> getAllTicketsWithoutNotes() {
        List<TicketsEntity> tickets = ticketRepository.findAll();
        tickets.forEach(ticket -> ticket.setNotes(null)); // Remove os comentários
        return tickets;
    }

    public List<TicketsEntity> getTicketsByStatus(String status) {
        return ticketRepository.findByStatus(status);
    }

    public List<TicketsEntity> getTicketsByPriority(String priority) {
        return ticketRepository.findByPriority(priority);
    }

    public List<TicketsEntity> getTicketsByAssignee(String assignee) {
        return ticketRepository.findByAssignee(assignee);
    }

    public List<TicketsEntity> getTicketsByNodeName(String nodeName) {
        return ticketRepository.findByNodeName(nodeName);
    }

    public List<TicketsEntity> getTicketsByStatusAndPriority(String status, String priority) {
        return ticketRepository.findByStatusAndPriority(status, priority);
    }

    public List<TicketsEntity> getTicketsByStatusAndAssignee(String status, String assignee) {
        return ticketRepository.findByStatusAndAssignee(status, assignee);
    }

    public List<TicketsEntity> getTicketsByStatusAndNodeName(String status, String nodeName) {
        return ticketRepository.findByStatusAndNodeName(status, nodeName);
    }

    public List<TicketsEntity> getTicketsByPriorityAndAssignee(String priority, String assignee) {
        return ticketRepository.findByPriorityAndAssignee(priority, assignee);
    }

    public List<TicketsEntity> getAllTickets() {
        return ticketRepository.findAll();
    }
    
    public TicketsEntity getTicketById(String id) {
        return ticketRepository.findById(id).orElse(null);
    }

    public TicketsEntity createTicket(String nameTicket, String status, String description, String priority, String assignee, String nodeName) {
        String nextTicket = generateNextTicket();
        TicketsEntity newTicket = new TicketsEntity();
        newTicket.setTicketID(nextTicket);
        newTicket.setNameTickets(nameTicket);
        newTicket.setStatus(status);
        newTicket.setDescription(description);
        newTicket.setPriority(priority);
        newTicket.setAssignee(assignee);
        newTicket.setNodeName(nodeName);
        newTicket.setCreatedAt(LocalDateTime.now());
        newTicket.setNotes(new ArrayList<>()); // Inicializa a lista de notas
        return ticketRepository.save(newTicket);
    }

    public TicketsEntity addNoteToTicket(String id, NoteEntity note) {
        return ticketRepository.findById(id).map(ticket -> {
            if (ticket.getNotes() == null) {
                ticket.setNotes(new ArrayList<>());
            }
            note.setCreatedDate(LocalDateTime.now()); // Define a data de criação
            noteRepository.save(note);
            ticket.getNotes().add(note);
            return ticketRepository.save(ticket);
        }).orElseThrow(() -> new RuntimeException("Ticket not found with id " + id));
    }

    private String generateNextTicket() {
        Optional<TicketsEntity> lastTicket = ticketRepository.findTopByOrderByTicketIDDesc();
    
        if (lastTicket.isPresent()) {
            String lastTicketID = lastTicket.get().getTicketID();
            
            if (lastTicketID != null && lastTicketID.matches("INC\\d{5}")) {
                int number = Integer.parseInt(lastTicketID.replace("INC", "")) + 1;
                return String.format("INC%05d", number);
            }
        }
        return "INC00001";  // Se não houver tickets anteriores
    }

    public TicketsEntity updateTicket(String id, String status, String priority, String assignee) {
        Optional<TicketsEntity> ticketOptional = ticketRepository.findById(id);
        if (ticketOptional.isPresent()) {
            TicketsEntity ticket = ticketOptional.get();
            ticket.setStatus(status);
            ticket.setPriority(priority);
            ticket.setAssignee(assignee);
            TicketsEntity updatedTicket = ticketRepository.save(ticket);
            if ("Resolvido".equals(status)) {
                rabbitMQProducer.sendMessage("Ticket resolved: " + updatedTicket.getTicketID());
            }
            return updatedTicket;
        } else {
            throw new RuntimeException("Ticket not found with id " + id);
        }
    }
}
