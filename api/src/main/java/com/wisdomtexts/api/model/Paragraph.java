package com.wisdomtexts.api.model;

import jakarta.persistence.*;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "paragraphs")
public class Paragraph {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookpart_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private BookPart bookPart;
    
    @Column(nullable = false)
    private String title;
    
    @Column(name = "latin_title")
    private String latinTitle;
    
    public Paragraph() {
    }
    
    public Paragraph(BookPart bookPart, String title, String latinTitle) {
        this.bookPart = bookPart;
        this.title = title;
        this.latinTitle = latinTitle;
    }
    
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public BookPart getBookPart() {
        return bookPart;
    }
    
    public void setBookPart(BookPart bookPart) {
        this.bookPart = bookPart;
    }
    
    // Convenience method to get bookPartId
    public UUID getBookPartId() {
        return bookPart != null ? bookPart.getId() : null;
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
}