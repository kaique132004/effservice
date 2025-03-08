package com.effservice.server.schema;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tickets")
public class TicketsEntity {
    @Id
    private String id;
    private String ticketID;
    private String nameTickets;
    private String status;
    private String description;
    private String priority;
    private String assignee;
    private String nodeName;
    
    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    private LocalDateTime closedAt;
    
    private List<NoteEntity> notes = new ArrayList<>();

    // Construtor vazio (necessário para o MongoDB)
    public TicketsEntity() {}

    // Construtor para facilitar a criação de tickets
    public TicketsEntity(String ticketID, String nameTickets, String status, String description,
                         String priority, String assignee, String nodeName, LocalDateTime updatedAt, LocalDateTime closedAt) {
        this.ticketID = ticketID;
        this.nameTickets = nameTickets;
        this.status = status;
        this.description = description;
        this.priority = priority;
        this.assignee = assignee;
        this.nodeName = nodeName;
        this.createdAt = LocalDateTime.now();
        this.notes = new ArrayList<>();
        this.updatedAt = updatedAt;
        this.closedAt = closedAt;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTicketID() { return ticketID; }
    public void setTicketID(String ticketID) { this.ticketID = ticketID; }

    public String getNameTickets() { return nameTickets; }
    public void setNameTickets(String nameTickets) { this.nameTickets = nameTickets; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = (status != null) ? status : "Assigned"; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getAssignee() { return assignee; }
    public void setAssignee(String assignee) { this.assignee = (assignee != null) ? assignee : "(empty)"; }

    public String getNodeName() { return nodeName; }
    public void setNodeName(String nodeName) { this.nodeName = nodeName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<NoteEntity> getNotes() { return notes; }
    public void setNotes(List<NoteEntity> notes) { this.notes = notes != null ? notes : new ArrayList<>(); }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public LocalDateTime getClosedAt() { return closedAt; }
    public void setClosedAt(LocalDateTime closedAt) { this.closedAt = closedAt; }
}
