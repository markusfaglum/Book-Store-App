package com.book.store.app.api.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.book.store.app.api.repo.BookRepo;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@DataJpaTest
@SpringJUnitConfig
public class BookTest {

    @Autowired
    private BookRepo bookRepo;

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testSaveAndRetrieveBook() {
        // Arrange
        Book book = new Book(0L, "Title", "Author", "EAN123456", 29.99, LocalDate.now());

        // Act
        Book savedBook = bookRepo.save(book);
        Book retrievedBook = bookRepo.findById(savedBook.getId()).orElse(null);

        // Assert
        assertNotNull(retrievedBook);
        assertEquals("Title", retrievedBook.getTitle());
        assertEquals("Author", retrievedBook.getAuthor());
        assertEquals("EAN123456", retrievedBook.getEan());
        assertEquals(29.99, retrievedBook.getPrice());
    }

    @Test
    public void testBookValidation() {
        // Arrange
        Book book = new Book(0L, "", "Author", "", -1.0, LocalDate.now());

        // Act
        Set<ConstraintViolation<Book>> violations = validator.validate(book);

        // Debugging output
        for (ConstraintViolation<Book> violation : violations) {
            System.out.println("Violation: " + violation.getPropertyPath() + " - " + violation.getMessage());
        }

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(3, violations.size()); 
    }


    @Test
    public void testSaveBookWithValidData() {
        // Arrange
        Book book = new Book(0L, "Valid Title", "Valid Author", "EAN123", 20.0, LocalDate.now());

        // Act
        Book savedBook = bookRepo.save(book);

        // Assert
        assertNotNull(savedBook);
        assertEquals("Valid Title", savedBook.getTitle());
        assertEquals("Valid Author", savedBook.getAuthor());
        assertEquals("EAN123", savedBook.getEan());
        assertEquals(20.0, savedBook.getPrice());
    }

    @Test
    public void testUpdateBook() {
        // Arrange
        Book book = new Book(0L, "Old Title", "Old Author", "EANOLD", 10.0, LocalDate.now());
        Book savedBook = bookRepo.save(book);

        // Update book details
        savedBook.setTitle("Updated Title");
        savedBook.setAuthor("Updated Author");
        savedBook.setEan("EANNEW");
        savedBook.setPrice(15.0);

        // Act
        bookRepo.save(savedBook);
        Book updatedBook = bookRepo.findById(savedBook.getId()).orElse(null);

        // Assert
        assertNotNull(updatedBook);
        assertEquals("Updated Title", updatedBook.getTitle());
        assertEquals("Updated Author", updatedBook.getAuthor());
        assertEquals("EANNEW", updatedBook.getEan());
        assertEquals(15.0, updatedBook.getPrice());
    }

    @Test
    public void testDeleteBook() {
        // Arrange
        Book book = new Book(0L, "Title to Delete", "Author", "EANDELETE", 25.0, LocalDate.now());
        Book savedBook = bookRepo.save(book);

        // Act
        bookRepo.delete(savedBook);
        Book deletedBook = bookRepo.findById(savedBook.getId()).orElse(null);

        // Assert
        assertNull(deletedBook);
    }
}