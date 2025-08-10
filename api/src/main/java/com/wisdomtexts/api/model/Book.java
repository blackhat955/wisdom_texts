package com.wisdomtexts.api.model;

import jakarta.persistence.*; // this for JPA
import java.util.UUID; // this one to make Unique id for the respected row

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
    
    // this one kick in whene book object is created with no data
    public Book() {
        this.authorId = UUID.randomUUID();
    }
    
//When bok object is created properly
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.authorId = UUID.randomUUID();
    }
    
// get and set for the values
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