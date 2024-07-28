package com.book.store.app.api.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.book.store.app.api.repo.OrderRepo;
import com.book.store.app.api.repo.BookRepo;
import com.book.store.app.api.repo.CustomerRepo;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@DataJpaTest
@SpringJUnitConfig
public class OrderTest {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private CustomerRepo customerRepo;

    private Validator validator;

    private Book testBook;
    private Customer testCustomer;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        // Create and save a test book
        testBook = new Book(0L, "Test Book", "Test Author", "EAN123456", 25.99, LocalDate.now());
        testBook = bookRepo.save(testBook);

        // Create and save a test customer
        testCustomer = new Customer(0L, "Test Customer", "123 Test St", "test@example.com", "password123");
        testCustomer = customerRepo.save(testCustomer);
    }

    @Test
    public void testSaveAndRetrieveOrder() {
        // Arrange
        Order order = new Order(0L, testBook, testCustomer, "Processing", LocalDateTime.now());

        // Act
        Order savedOrder = orderRepo.save(order);
        Order retrievedOrder = orderRepo.findById(savedOrder.getId()).orElse(null);

        // Assert
        assertNotNull(retrievedOrder);
        assertEquals(testBook.getId(), retrievedOrder.getBook().getId());
        assertEquals(testCustomer.getId(), retrievedOrder.getCustomer().getId());
        assertEquals("Processing", retrievedOrder.getStatus());
        assertEquals(savedOrder.getOrderTime(), retrievedOrder.getOrderTime());
    }

    @Test
    public void testOrderValidation() {
        // Arrange
        Order order = new Order(0L, null, null, "", null);

        // Act
        Set<ConstraintViolation<Order>> violations = validator.validate(order);

        // Debugging output
        for (ConstraintViolation<Order> violation : violations) {
            System.out.println("Violation: " + violation.getPropertyPath() + " - " + violation.getMessage());
        }

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(4, violations.size()); // Adjust based on the actual constraints
    }

    @Test
    public void testSaveOrderWithValidData() {
        // Arrange
        Order order = new Order(0L, testBook, testCustomer, "Completed", LocalDateTime.now());

        // Act
        Order savedOrder = orderRepo.save(order);

        // Assert
        assertNotNull(savedOrder);
        assertEquals(testBook.getId(), savedOrder.getBook().getId());
        assertEquals(testCustomer.getId(), savedOrder.getCustomer().getId());
        assertEquals("Completed", savedOrder.getStatus());
        assertEquals(order.getOrderTime(), savedOrder.getOrderTime());
    }

    @Test
    public void testUpdateOrder() {
        // Arrange
        Order order = new Order(0L, testBook, testCustomer, "Pending", LocalDateTime.now());
        Order savedOrder = orderRepo.save(order);

        // Update order details
        savedOrder.setStatus("Shipped");

        // Act
        orderRepo.save(savedOrder);
        Order updatedOrder = orderRepo.findById(savedOrder.getId()).orElse(null);

        // Assert
        assertNotNull(updatedOrder);
        assertEquals("Shipped", updatedOrder.getStatus());
    }

    @Test
    public void testDeleteOrder() {
        // Arrange
        Order order = new Order(0L, testBook, testCustomer, "Cancelled", LocalDateTime.now());
        Order savedOrder = orderRepo.save(order);

        // Act
        orderRepo.delete(savedOrder);
        Order deletedOrder = orderRepo.findById(savedOrder.getId()).orElse(null);

        // Assert
        assertNull(deletedOrder);
    }
}
