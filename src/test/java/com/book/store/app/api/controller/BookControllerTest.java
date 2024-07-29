package com.book.store.app.api.controller;

import com.book.store.app.api.entity.Book;
import com.book.store.app.api.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    void testGetAllBooks() throws Exception {
        // Arrange
        Book book1 = new Book("Title1", "Author1", "EAN1", 10.0, "2024-07-29");
        Book book2 = new Book("Title2", "Author2", "EAN2", 15.0, "2024-07-30");
        when(bookService.getAllBooks()).thenReturn(Arrays.asList(book1, book2));

        // Act & Assert
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title1"))
                .andExpect(jsonPath("$[1].title").value("Title2"));
    }

    @Test
    void testGetBookById() throws Exception {
        // Arrange
        Book book = new Book("Title", "Author", "EAN", 20.0, "2024-07-29");
        when(bookService.getBookById(anyLong())).thenReturn(book);

        // Act & Assert
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"));
    }

    @Test
    void testCreateBook() throws Exception {
        // Arrange
        Book book = new Book("Title", "Author", "EAN", 25.0, "2024-07-29");
        when(bookService.saveBook(any(Book.class))).thenReturn(book);

        // Act & Assert
        mockMvc.perform(post("/api/books")
                .contentType("application/json")
                .content("{\"title\":\"Title\",\"author\":\"Author\",\"ean\":\"EAN\",\"price\":25.0,\"publishingDate\":\"2024-07-29\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"));
    }

    @Test
    void testUpdateBook() throws Exception {
        // Arrange
        Book existingBook = new Book("Old Title", "Old Author", "Old EAN", 30.0, "2024-07-29");
        Book updatedBook = new Book("New Title", "New Author", "New EAN", 35.0, "2024-07-30");
        when(bookService.getBookById(anyLong())).thenReturn(existingBook);
        when(bookService.updateBook(anyLong(), any(Book.class))).thenReturn(updatedBook);

        // Act & Assert
        mockMvc.perform(put("/api/books/1")
                .contentType("application/json")
                .content("{\"title\":\"New Title\",\"author\":\"New Author\",\"ean\":\"New EAN\",\"price\":35.0,\"publishingDate\":\"2024-07-30\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"));
    }

    @Test
    void testDeleteBook() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk());
    }
}

