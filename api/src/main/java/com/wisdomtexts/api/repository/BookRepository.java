package com.wisdomtexts.api.repository;

import com.wisdomtexts.api.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> { // this interface provide a bridge which give method like, save, findById, findAll and these methods
    
    Optional<Book> findByTitle(String title);
}