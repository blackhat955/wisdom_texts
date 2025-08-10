package com.wisdomtexts.api.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisdomtexts.api.model.Book;
import com.wisdomtexts.api.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private Book testBook;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testBook = new Book("Test Title", "Test Author");
        testBook.setId(testId);
    }

    @Test
    void testCreateBook() throws Exception {
        when(bookService.saveBook(any(Book.class))).thenReturn(testBook);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBook)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.id").value(testId.toString()));

        verify(bookService, times(1)).saveBook(any(Book.class));
    }

    @Test
    void testCreateBookError() throws Exception {
        when(bookService.saveBook(any(Book.class))).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBook)))
                .andExpect(status().isInternalServerError());

        verify(bookService, times(1)).saveBook(any(Book.class));
    }

    @Test
    void testGetAllBooks() throws Exception {
        List<Book> books = Arrays.asList(testBook, new Book("Another Title", "Another Author"));
        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Test Title"))
                .andExpect(jsonPath("$[1].title").value("Another Title"));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testGetAllBooksEmpty() throws Exception {
        when(bookService.getAllBooks()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testGetAllBooksError() throws Exception {
        when(bookService.getAllBooks()).thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isInternalServerError());

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void testGetBookById() throws Exception {
        when(bookService.getBookById(testId)).thenReturn(Optional.of(testBook));

        mockMvc.perform(get("/api/books/{id}", testId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Title"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.id").value(testId.toString()));

        verify(bookService, times(1)).getBookById(testId);
    }

    @Test
    void testGetBookByIdNotFound() throws Exception {
        when(bookService.getBookById(testId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/{id}", testId))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).getBookById(testId);
    }

    @Test
    void testUpdateBook() throws Exception {
        Book updatedBook = new Book("Updated Title", "Updated Author");
        updatedBook.setId(testId);
        when(bookService.updateBook(eq(testId), any(Book.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/api/books/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.author").value("Updated Author"))
                .andExpect(jsonPath("$.id").value(testId.toString()));

        verify(bookService, times(1)).updateBook(eq(testId), any(Book.class));
    }

    @Test
    void testUpdateBookNotFound() throws Exception {
        when(bookService.updateBook(eq(testId), any(Book.class)))
                .thenThrow(new RuntimeException("Book not found with id: " + testId));

        mockMvc.perform(put("/api/books/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBook)))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).updateBook(eq(testId), any(Book.class));
    }

    @Test
    void testUpdateBookException() throws Exception {
        when(bookService.updateBook(eq(testId), any(Book.class)))
                .thenThrow(new IllegalStateException("Unexpected error"));

        mockMvc.perform(put("/api/books/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBook)))
                .andExpect(status().isNotFound());

        verify(bookService, times(1)).updateBook(eq(testId), any(Book.class));
    }

    @Test
    void testDeleteBook() throws Exception {
        doNothing().when(bookService).deleteBook(testId);

        mockMvc.perform(delete("/api/books/{id}", testId))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBook(testId);
    }

    @Test
    void testDeleteBookError() throws Exception {
        doThrow(new RuntimeException("Database error")).when(bookService).deleteBook(testId);

        mockMvc.perform(delete("/api/books/{id}", testId))
                .andExpect(status().isInternalServerError());

        verify(bookService, times(1)).deleteBook(testId);
    }

    @Test
    void testCreateBookInvalidJson() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("invalid json"))
                .andExpect(status().isBadRequest());

        verify(bookService, never()).saveBook(any(Book.class));
    }

    @Test
    void testUpdateBookInvalidJson() throws Exception {
        mockMvc.perform(put("/api/books/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("invalid json"))
                .andExpect(status().isBadRequest());

        verify(bookService, never()).updateBook(any(UUID.class), any(Book.class));
    }

    @Test
    void testGetBookByIdInvalidUUID() throws Exception {
        mockMvc.perform(get("/api/books/{id}", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(bookService, never()).getBookById(any(UUID.class));
    }
}