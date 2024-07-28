package com.book.store.app.api.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.book.store.app.api.entity.Customer;
import com.book.store.app.api.repo.CustomerRepo;

@SpringBootTest
@SpringJUnitConfig
class CustomerServiceTest {
    
    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepo customerRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCustomers() {
        // Arrange
        Customer customer1 = new Customer(1L, "John Doe", "123 Main St", "john@example.com", "password123");
        Customer customer2 = new Customer(2L, "Jane Doe", "456 Elm St", "jane@example.com", "password456");
        when(customerRepo.findAll()).thenReturn(Arrays.asList(customer1, customer2));

        // Act
        List<Customer> customers = customerService.getAllCustomers();

        // Assert
        assertNotNull(customers);
        assertEquals(2, customers.size());
        verify(customerRepo, times(1)).findAll();
    }

    @Test
    void testGetCustomerById_Found() {
        // Arrange
        Customer customer = new Customer(1L, "John Doe", "123 Main St", "john@example.com", "password123");
        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));

        // Act
        Customer foundCustomer = customerService.getCustomerById(1L);

        // Assert
        assertNotNull(foundCustomer);
        assertEquals("John Doe", foundCustomer.getName());
        verify(customerRepo, times(1)).findById(1L);
    }

    @Test
    void testGetCustomerById_NotFound() {
        // Arrange
        when(customerRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            customerService.getCustomerById(1L);
        });
        assertEquals("Customer not found with id: 1", exception.getMessage());
        verify(customerRepo, times(1)).findById(1L);
    }

    @Test
    void testSaveCustomer() {
        // Arrange
        Customer customer = new Customer(1L, "John Doe", "123 Main St", "john@example.com", "password123");
        when(customerRepo.save(any(Customer.class))).thenReturn(customer);

        // Act
        Customer savedCustomer = customerService.saveCustomer(customer);

        // Assert
        assertNotNull(savedCustomer);
        assertEquals("John Doe", savedCustomer.getName());
        verify(customerRepo, times(1)).save(customer);
    }

    @Test
    void testUpdateCustomer() {
        // Arrange
        Customer existingCustomer = new Customer(1L, "John Doe", "123 Main St", "john@example.com", "password123");
        Customer updatedCustomerData = new Customer(1L, "John Smith", "789 Oak St", "john.smith@example.com", "newpassword123");
        when(customerRepo.findById(1L)).thenReturn(Optional.of(existingCustomer));
        when(customerRepo.save(existingCustomer)).thenReturn(existingCustomer);

        // Act
        Customer updatedCustomer = customerService.updateCustomer(1L, updatedCustomerData);

        // Assert
        assertNotNull(updatedCustomer);
        assertEquals("John Smith", updatedCustomer.getName());
        assertEquals("newpassword123", updatedCustomer.getPassword());
        verify(customerRepo, times(1)).findById(1L);
        verify(customerRepo, times(1)).save(existingCustomer);
    }

    @Test
    void testDeleteCustomer() {
        // Arrange
        Customer customer = new Customer(1L, "John Doe", "123 Main St", "john@example.com", "password123");
        when(customerRepo.findById(1L)).thenReturn(Optional.of(customer));

        // Act
        customerService.deleteCustomer(1L);

        // Assert
        verify(customerRepo, times(1)).delete(customer);
    }

    @Test
    void testDeleteCustomer_NotFound() {
        // Arrange
        when(customerRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            customerService.deleteCustomer(1L);
        });
        assertEquals("Customer not found with id: 1", exception.getMessage());
        verify(customerRepo, times(1)).findById(1L);
    }
}
