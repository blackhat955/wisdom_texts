package com.wisdomtexts.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "book_parts")
public class BookPart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "book_id", nullable = false)
    @JsonProperty("book_id")
    private UUID bookId;
    
    @Column(name = "ParentsName")
    @JsonProperty("ParentsName")
    private String parentsName;
    
    @Column(name = "prents_id")
    @JsonProperty("prents_id")
    @JoinColumn(name = "prents_id", referencedColumnName = "id")
    private UUID prentsId;
    
    @Column(nullable = false)
    @JsonProperty("title")
    private String title;
    
    @Column(name = "latin_title")
    @JsonProperty("latin_title")
    private String latinTitle;
    
    // Default constructor
    public BookPart() {
    }
    
    // Constructor for root level parts (prents_id = NULL)
    public BookPart(UUID bookId, String title, String latinTitle) {
        this.bookId = bookId;
        this.title = title;
        this.latinTitle = latinTitle;
        this.prentsId = null; // Root level
    }
    
    // Constructor for child parts (with parent)
    public BookPart(UUID bookId, String title, String latinTitle, String parentsName, UUID prentsId) {
        this.bookId = bookId;
        this.title = title;
        this.latinTitle = latinTitle;
        this.parentsName = parentsName;
        this.prentsId = prentsId;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getBookId() {
        return bookId;
    }
    
    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }
    
    public String getParentsName() {
        return parentsName;
    }
    
    public void setParentsName(String parentsName) {
        this.parentsName = parentsName;
    }
    
    public UUID getPrentsId() {
        return prentsId;
    }
    
    public void setPrentsId(UUID prentsId) {
        this.prentsId = prentsId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getLatinTitle() {
        return latinTitle;
    }
    
    public void setLatinTitle(String latinTitle) {
        this.latinTitle = latinTitle;
    }
    
    // Helper methods
    @JsonIgnore
    public boolean isRoot() {
        return prentsId == null;
    }
}