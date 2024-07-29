package com.book.store.app.api.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

import com.book.store.app.api.entity.Order;
import com.book.store.app.api.entity.Book;
import com.book.store.app.api.entity.Customer;
import com.book.store.app.api.repo.OrderRepo;

@SpringBootTest
@SpringJUnitConfig
class OrderServiceTest {
	
    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepo orderRepo;
    
    

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllOrders() {
        // Arrange
    	Book book1 = new Book("Title", "Author", "EAN", 20.0, "time");
    	Book book2 = new Book("Title2", "Author2", "EAN2", 20.2, "time");
    	Customer customer1 = new Customer("Name", "Address", "Email", "Password");
    	Customer customer2 = new Customer("Name2", "Address2", "Email2", "Password2");
        Order order1 = new Order(book1, customer1, "Status1", LocalDateTime.now());
        Order order2 = new Order(book2, customer2, "Status2", LocalDateTime.now());
        when(orderRepo.findAll()).thenReturn(Arrays.asList(order1, order2));

        // Act
        List<Order> orders = orderService.getAllOrders();

        // Assert
        assertNotNull(orders);
        assertEquals(2, orders.size());
        verify(orderRepo, times(1)).findAll();
    }

    @Test
    void testGetOrderById_Found() {
        // Arrange
    	Book book1 = new Book("Title", "Author", "EAN", 20.0, "time");
    	Customer customer1 = new Customer("Name", "Address", "Email", "Password");
        Order order = new Order(book1, customer1, "Status", LocalDateTime.now());
        when(orderRepo.findById(order.getId())).thenReturn(Optional.of(order));

        // Act
        Order foundOrder = orderService.getOrderById(order.getId());

        // Assert
        assertNotNull(foundOrder);
        assertEquals("Status", foundOrder.getStatus());
        verify(orderRepo, times(1)).findById(order.getId());
    }

    @Test
    void testGetOrderById_NotFound() {
        // Arrange
        when(orderRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderService.getOrderById(1L);
        });
        assertEquals("Order not found with id: 1", exception.getMessage());
        verify(orderRepo, times(1)).findById(1L);
    }

    @Test
    void testSaveOrder() {
        // Arrange
    	Book book1 = new Book("Title", "Author", "EAN", 20.0, "time");
    	Customer customer1 = new Customer("Name", "Address", "Email", "Password");
        Order order = new Order(book1, customer1, "Status", LocalDateTime.now());
        when(orderRepo.save(any(Order.class))).thenReturn(order);

        // Act
        Order savedOrder = orderService.saveOrder(order);

        // Assert
        assertNotNull(savedOrder);
        assertEquals("Status", savedOrder.getStatus());
        verify(orderRepo, times(1)).save(order);
    }

    @Test
    void testUpdateOrder() {
        // Arrange
    	Book book1 = new Book("Title", "Author", "EAN", 20.0, "time");
    	Customer customer1 = new Customer("Name", "Address", "Email", "Password");
        Order existingOrder = new Order(book1, customer1, "Old Status", LocalDateTime.now());
        Order updatedOrderData = new Order(book1, customer1, "New Status", LocalDateTime.now());
        when(orderRepo.findById(existingOrder.getId())).thenReturn(Optional.of(existingOrder));
        when(orderRepo.save(existingOrder)).thenReturn(existingOrder);

        // Act
        Order updatedOrder = orderService.updateOrder(existingOrder.getId(), updatedOrderData);

        // Assert
        assertNotNull(updatedOrder);
        assertEquals("New Status", updatedOrder.getStatus());
        verify(orderRepo, times(1)).findById(existingOrder.getId());
        verify(orderRepo, times(1)).save(existingOrder);
    }

    @Test
    void testDeleteOrder() {
        // Arrange
    	Book book1 = new Book("Title", "Author", "EAN", 20.0, "time");
    	Customer customer1 = new Customer("Name", "Address", "Email", "Password");
        Order order = new Order(book1, customer1, "Status", LocalDateTime.now());
        when(orderRepo.findById(order.getId())).thenReturn(Optional.of(order));

        // Act
        orderService.deleteOrder(order.getId());

        // Assert
        verify(orderRepo, times(1)).delete(order);
    }

    @Test
    void testDeleteOrder_NotFound() {
        // Arrange
        when(orderRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderService.deleteOrder(1L);
        });
        assertEquals("Order not found with id: 1", exception.getMessage());
        verify(orderRepo, times(1)).findById(1L);
    }
}