package com.wisdomtexts.api.services;

import com.wisdomtexts.api.model.Book;
import com.wisdomtexts.api.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testBook = new Book("Test Title", "Test Author");
        testBook.setId(testId);
    }

    @Test
    void testSaveBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        Book savedBook = bookService.saveBook(testBook);

        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isEqualTo(testId);
        assertThat(savedBook.getTitle()).isEqualTo("Test Title");
        assertThat(savedBook.getAuthor()).isEqualTo("Test Author");
        verify(bookRepository, times(1)).save(testBook);
    }

    @Test
    void testSaveBookNull() {
        Book nullBook = null;
        when(bookRepository.save(nullBook)).thenReturn(null);

        Book result = bookService.saveBook(nullBook);

        assertThat(result).isNull();
        verify(bookRepository, times(1)).save(nullBook);
    }

    @Test
    void testGetAllBooks() {
        Book book1 = new Book("Title 1", "Author 1");
        Book book2 = new Book("Title 2", "Author 2");
        List<Book> expectedBooks = Arrays.asList(book1, book2);
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        List<Book> actualBooks = bookService.getAllBooks();

        assertThat(actualBooks).isNotNull();
        assertThat(actualBooks).hasSize(2);
        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetAllBooksEmpty() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList());

        List<Book> actualBooks = bookService.getAllBooks();

        assertThat(actualBooks).isNotNull();
        assertThat(actualBooks).isEmpty();
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetBookById() {
        when(bookRepository.findById(testId)).thenReturn(Optional.of(testBook));

        Optional<Book> result = bookService.getBookById(testId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(testId);
        assertThat(result.get().getTitle()).isEqualTo("Test Title");
        assertThat(result.get().getAuthor()).isEqualTo("Test Author");
        verify(bookRepository, times(1)).findById(testId);
    }

    @Test
    void testGetBookByIdNotFound() {
        when(bookRepository.findById(testId)).thenReturn(Optional.empty());

        Optional<Book> result = bookService.getBookById(testId);

        assertThat(result).isEmpty();
        verify(bookRepository, times(1)).findById(testId);
    }

    @Test
    void testGetBookByIdNull() {
        UUID nullId = null;
        when(bookRepository.findById(nullId)).thenReturn(Optional.empty());

        Optional<Book> result = bookService.getBookById(nullId);

        assertThat(result).isEmpty();
        verify(bookRepository, times(1)).findById(nullId);
    }

    @Test
    void testDeleteBook() {
        doNothing().when(bookRepository).deleteById(testId);

        bookService.deleteBook(testId);

        verify(bookRepository, times(1)).deleteById(testId);
    }

    @Test
    void testDeleteBookNull() {
        UUID nullId = null;
        doNothing().when(bookRepository).deleteById(nullId);

        bookService.deleteBook(nullId);

        verify(bookRepository, times(1)).deleteById(nullId);
    }

    @Test
    void testUpdateBook() {
        Book updatedBookDetails = new Book("Updated Title", "Updated Author");
        UUID updatedAuthorId = UUID.randomUUID();
        updatedBookDetails.setAuthorId(updatedAuthorId);
        
        Book existingBook = new Book("Original Title", "Original Author");
        existingBook.setId(testId);
        
        Book expectedUpdatedBook = new Book("Updated Title", "Updated Author");
        expectedUpdatedBook.setId(testId);
        expectedUpdatedBook.setAuthorId(updatedAuthorId);
        
        when(bookRepository.findById(testId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(expectedUpdatedBook);

        Book result = bookService.updateBook(testId, updatedBookDetails);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testId);
        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getAuthor()).isEqualTo("Updated Author");
        assertThat(result.getAuthorId()).isEqualTo(updatedAuthorId);
        
        verify(bookRepository, times(1)).findById(testId);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testUpdateBookNotFound() {
        Book updatedBookDetails = new Book("Updated Title", "Updated Author");
        when(bookRepository.findById(testId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.updateBook(testId, updatedBookDetails))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Book not found with id: " + testId);
        
        verify(bookRepository, times(1)).findById(testId);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testUpdateBookNullDetails() {
        Book existingBook = new Book("Original Title", "Original Author");
        existingBook.setId(testId);
        
        when(bookRepository.findById(testId)).thenReturn(Optional.of(existingBook));

        assertThatThrownBy(() -> bookService.updateBook(testId, null))
                .isInstanceOf(NullPointerException.class);
        
        verify(bookRepository, times(1)).findById(testId);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void testUpdateBookPartial() {
        Book updatedBookDetails = new Book();
        updatedBookDetails.setTitle("Updated Title");
        updatedBookDetails.setAuthor(null);
        updatedBookDetails.setAuthorId(null);
        
        Book existingBook = new Book("Original Title", "Original Author");
        existingBook.setId(testId);
        UUID originalAuthorId = UUID.randomUUID();
        existingBook.setAuthorId(originalAuthorId);
        
        Book expectedUpdatedBook = new Book("Updated Title", null);
        expectedUpdatedBook.setId(testId);
        expectedUpdatedBook.setAuthorId(null);
        
        when(bookRepository.findById(testId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(expectedUpdatedBook);

        Book result = bookService.updateBook(testId, updatedBookDetails);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testId);
        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getAuthor()).isNull();
        assertThat(result.getAuthorId()).isNull();
        
        verify(bookRepository, times(1)).findById(testId);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testUpdateBookPreserveId() {
        Book updatedBookDetails = new Book("Updated Title", "Updated Author");
        UUID differentId = UUID.randomUUID();
        updatedBookDetails.setId(differentId);
        
        Book existingBook = new Book("Original Title", "Original Author");
        existingBook.setId(testId);
        
        when(bookRepository.findById(testId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book bookToSave = invocation.getArgument(0);
            assertThat(bookToSave.getId()).isEqualTo(testId);
            return bookToSave;
        });

        Book result = bookService.updateBook(testId, updatedBookDetails);

        assertThat(result.getId()).isEqualTo(testId);
        verify(bookRepository, times(1)).findById(testId);
        verify(bookRepository, times(1)).save(any(Book.class));
    }
}