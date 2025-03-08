package com.effservice.server.schema;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "notes")
public class NoteEntity {
    @Id
    private String id;
    private String author;
    private String content;

    @CreatedDate
    private LocalDateTime createdDate;

    // Construtor padrão
    public NoteEntity() {
    }

    @JsonCreator
    public NoteEntity(@JsonProperty("author") String author, @JsonProperty("content") String content) {
        this.author = author;
        this.content = content;
        this.createdDate = LocalDateTime.now();
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = (author != null) ? author : "Anonymous"; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = (content != null) ? content : ""; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}
