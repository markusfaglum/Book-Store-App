package com.book.store.app.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.book.store.app.api.entity.Book;
import com.book.store.app.api.repo.BookRepo;


@SpringBootTest
@SpringJUnitConfig
class BookServiceTest {
	
	@InjectMocks
    private BookService bookService;

    @Mock
    private BookRepo bookRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        // Arrange
        Book book1 = new Book(1L, "Title1", "Author1", "EAN1", 10.0, LocalDate.now());
        Book book2 = new Book(2L, "Title2", "Author2", "EAN2", 15.0, LocalDate.now());
        when(bookRepo.findAll()).thenReturn(Arrays.asList(book1, book2));

        // Act
        List<Book> books = bookService.getAllBooks();

        // Assert
        assertNotNull(books);
        assertEquals(2, books.size());
        verify(bookRepo, times(1)).findAll();
    }

    @Test
    void testGetBookById_Found() {
        // Arrange
        Book book = new Book(1L, "Title", "Author", "EAN", 10.0, LocalDate.now());
        
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));

        // Act
        Book foundBook = bookService.getBookById(1L);

        // Assert
        assertNotNull(foundBook);
        assertEquals("Title", foundBook.getTitle());
        verify(bookRepo, times(1)).findById(1L);
    }

    @Test
    void testGetBookById_NotFound() {
        // Arrange
        when(bookRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            bookService.getBookById(1L);
        });
        assertEquals("Book not found with id: 1", exception.getMessage());
        verify(bookRepo, times(1)).findById(1L);
    }

    @Test
    void testSaveBook() {
        // Arrange
        Book book = new Book(1L, "Title", "Author", "EAN", 10.0, LocalDate.now());
        when(bookRepo.save(any(Book.class))).thenReturn(book);

        // Act
        Book savedBook = bookService.saveBook(book);

        // Assert
        assertNotNull(savedBook);
        assertEquals("Title", savedBook.getTitle());
        verify(bookRepo, times(1)).save(book);
    }

    @Test
    void testUpdateBook() {
        // Arrange
        Book existingBook = new Book(1L, "Old Title", "Old Author", "Old EAN", 20.0, LocalDate.now());
        Book updatedBookData = new Book(1L, "New Title", "New Author", "New EAN", 25.0, LocalDate.now());
        when(bookRepo.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepo.save(existingBook)).thenReturn(existingBook);

        // Act
        Book updatedBook = bookService.updateBook(1L, updatedBookData);

        // Assert
        assertNotNull(updatedBook);
        assertEquals("New Title", updatedBook.getTitle());
        assertEquals(25.0, updatedBook.getPrice());
        verify(bookRepo, times(1)).findById(1L);
        verify(bookRepo, times(1)).save(existingBook);
    }

    @Test
    void testDeleteBook() {
        // Arrange
        Book book = new Book(1L, "Title", "Author", "EAN", 10.0, LocalDate.now());
        when(bookRepo.findById(1L)).thenReturn(Optional.of(book));

        // Act
        bookService.deleteBook(1L);

        // Assert
        verify(bookRepo, times(1)).delete(book);
    }

    @Test
    void testDeleteBook_NotFound() {
        // Arrange
        when(bookRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            bookService.deleteBook(1L);
        });
        assertEquals("Book not found with id: 1", exception.getMessage());
        verify(bookRepo, times(1)).findById(1L);
    }
}
