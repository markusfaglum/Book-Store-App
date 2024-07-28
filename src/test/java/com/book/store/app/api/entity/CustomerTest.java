package com.book.store.app.api.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.book.store.app.api.repo.CustomerRepo;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@DataJpaTest
@SpringJUnitConfig
public class CustomerTest {

    @Autowired
    private CustomerRepo customerRepo;

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testSaveAndRetrieveCustomer() {
        // Arrange
        Customer customer = new Customer(0L, "John Doe", "123 Main St", "john.doe@example.com", "securePassword123");

        // Act
        Customer savedCustomer = customerRepo.save(customer);
        Customer retrievedCustomer = customerRepo.findById(savedCustomer.getId()).orElse(null);

        // Assert
        assertNotNull(retrievedCustomer);
        assertEquals("John Doe", retrievedCustomer.getName());
        assertEquals("123 Main St", retrievedCustomer.getAddress());
        assertEquals("john.doe@example.com", retrievedCustomer.getEmail());
        assertEquals("securePassword123", retrievedCustomer.getPassword());
    }

    @Test
    public void testCustomerValidation() {
        // Arrange
        Customer customer = new Customer(0L, "", "", "", "kk");

        // Act
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);

        // Debugging output
        for (ConstraintViolation<Customer> violation : violations) {
            System.out.println("Violation: " + violation.getPropertyPath() + " - " + violation.getMessage());
        }

        // Assert
        assertFalse(violations.isEmpty());
        assertEquals(4, violations.size()); 
    }

    @Test
    public void testSaveCustomerWithValidData() {
        // Arrange
        Customer customer = new Customer(0L, "Jane Doe", "456 Elm St", "jane.doe@example.com", "anotherSecurePassword");

        // Act
        Customer savedCustomer = customerRepo.save(customer);

        // Assert
        assertNotNull(savedCustomer);
        assertEquals("Jane Doe", savedCustomer.getName());
        assertEquals("456 Elm St", savedCustomer.getAddress());
        assertEquals("jane.doe@example.com", savedCustomer.getEmail());
        assertEquals("anotherSecurePassword", savedCustomer.getPassword());
    }

    @Test
    public void testUpdateCustomer() {
        // Arrange
        Customer customer = new Customer(0L, "Old Name", "Old Address", "old.email@example.com", "oldPassword");
        Customer savedCustomer = customerRepo.save(customer);

        // Update customer details
        savedCustomer.setName("New Name");
        savedCustomer.setAddress("New Address");
        savedCustomer.setEmail("new.email@example.com");
        savedCustomer.setPassword("newPassword");

        // Act
        customerRepo.save(savedCustomer);
        Customer updatedCustomer = customerRepo.findById(savedCustomer.getId()).orElse(null);

        // Assert
        assertNotNull(updatedCustomer);
        assertEquals("New Name", updatedCustomer.getName());
        assertEquals("New Address", updatedCustomer.getAddress());
        assertEquals("new.email@example.com", updatedCustomer.getEmail());
        assertEquals("newPassword", updatedCustomer.getPassword());
    }

    @Test
    public void testDeleteCustomer() {
        // Arrange
        Customer customer = new Customer(0L, "Delete Name", "Delete Address", "delete.email@example.com", "deletePassword");
        Customer savedCustomer = customerRepo.save(customer);

        // Act
        customerRepo.delete(savedCustomer);
        Customer deletedCustomer = customerRepo.findById(savedCustomer.getId()).orElse(null);

        // Assert
        assertNull(deletedCustomer);
    }
}
