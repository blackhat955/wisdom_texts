package com.wisdomtexts.api.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "books")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String author;
    
    @Column(name = "author_id", nullable = false)
    private UUID authorId;
    
    
    public Book() {
        this.authorId = UUID.randomUUID();
    }
    

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.authorId = UUID.randomUUID();
    }
    

    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public UUID getAuthorId() {
        return authorId;
    }
    
    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }
}