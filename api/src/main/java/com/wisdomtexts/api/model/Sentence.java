package com.wisdomtexts.api.model;

import jakarta.persistence.*;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "sentences")
public class Sentence {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paragraph_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Paragraph paragraph;
    
    @Column(name = "paragraph_id", nullable = false, insertable = false, updatable = false)
    private UUID paragraphId;
    
    @Column(name = "text", nullable = false, columnDefinition = "TEXT")
    private String text;
    
    @Column(name = "latin_title")
    private String latinTitle;
    
    public Sentence() {
    }
    
    public Sentence(UUID paragraphId, String text, String latinTitle) {
        this.paragraphId = paragraphId;
        this.text = text;
        this.latinTitle = latinTitle;
    }
    
    public Sentence(Paragraph paragraph, String text, String latinTitle) {
        this.paragraph = paragraph;
        this.paragraphId = paragraph.getId();
        this.text = text;
        this.latinTitle = latinTitle;
    }
    
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getParagraphId() {
        return paragraphId;
    }
    
    public void setParagraphId(UUID paragraphId) {
        this.paragraphId = paragraphId;
    }
    
    public Paragraph getParagraph() {
        return paragraph;
    }
    
    public void setParagraph(Paragraph paragraph) {
        this.paragraph = paragraph;
        if (paragraph != null) {
            this.paragraphId = paragraph.getId();
        }
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public String getLatinTitle() {
        return latinTitle;
    }
    
    public void setLatinTitle(String latinTitle) {
        this.latinTitle = latinTitle;
    }
}