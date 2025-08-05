package com.wisdomtexts.api.services;

import com.wisdomtexts.api.model.Book;
import com.wisdomtexts.api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }
    
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public Optional<Book> getBookById(UUID id) {
        return bookRepository.findById(id);
    }
    
    public void deleteBook(UUID id) {
        bookRepository.deleteById(id);
    }
    
    public Book updateBook(UUID id, Book bookDetails) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setAuthorId(bookDetails.getAuthorId());
        
        return bookRepository.save(book);
    }
}