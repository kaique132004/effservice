package com.effservice.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.effservice.server.schema.NoteEntity;
import com.effservice.server.schema.TicketsEntity;
import com.effservice.server.service.TicketService;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")  // Permite chamadas externas (ajuste conforme necessário)
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping
    public ResponseEntity<List<TicketsEntity>> getAllTicketsBasic() {
        List<TicketsEntity> tickets = ticketService.getAllTicketsWithoutNotes();
        return ResponseEntity.ok(tickets);
    }

    // GET completo (com comentários)
    @GetMapping("/full")
    public ResponseEntity<List<TicketsEntity>> getAllTicketsFull() {
        List<TicketsEntity> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }

    @PostMapping
    public ResponseEntity<?> createTicket(@RequestBody TicketsEntity ticket) {
        if (ticket.getNameTickets() == null || ticket.getStatus() == null || ticket.getPriority() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Campos obrigatórios: nameTickets, status e priority.");
        }
        
        TicketsEntity createdTicket = ticketService.createTicket(ticket.getNameTickets(), ticket.getStatus(), 
                ticket.getDescription(), ticket.getPriority(), ticket.getAssignee(), ticket.getNodeName());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TicketsEntity>> getTicketsByStatus(@PathVariable String status) {
        List<TicketsEntity> tickets = ticketService.getTicketsByStatus(status);
        return ResponseEntity.ok(tickets);
    }

    @PostMapping("/{id}/addNote")
    public ResponseEntity<?> addNoteToTicket(@PathVariable String id, @RequestBody NoteEntity note) {
        try {
            NoteEntity newNote = new NoteEntity(note.getAuthor(), note.getContent());
            TicketsEntity updatedTicket = ticketService.addNoteToTicket(id, newNote);
            return ResponseEntity.ok(updatedTicket);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateTicket(@PathVariable String id, @RequestBody TicketsEntity ticket) {
        try {
            TicketsEntity updatedTicket = ticketService.updateTicket(id, ticket.getStatus(), ticket.getPriority(), ticket.getAssignee());
            return ResponseEntity.ok(updatedTicket);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
