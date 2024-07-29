package com.book.store.app.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.book.store.app.api.entity.Book;
import com.book.store.app.api.entity.Customer;
import com.book.store.app.api.entity.Order;
import com.book.store.app.api.service.OrderService;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void testGetAllOrders() throws Exception {
        // Arrange
        Order order = new Order(
            new Book("Sample Book", "Sample Author", "1234567890", 19.99, "2024-07-29"),
            new Customer("John Doe", "123 Main St", "john.doe@example.com", "securePassword123"),
            "Shipped", LocalDateTime.now()
        );
        List<Order> orders = List.of(order);
        when(orderService.getAllOrders()).thenReturn(orders);

        // Act & Assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/orders"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].status").value("Shipped"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].orderTime").exists()) 
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].book.publishingDate").value("2024-07-29"));
    }

    @Test
    void testGetOrderById() throws Exception {
        // Arrange
        Order order = new Order(
            new Book("Sample Book", "Sample Author", "1234567890", 19.99, "2024-07-29"),
            new Customer("John Doe", "123 Main St", "john.doe@example.com", "securePassword123"),
            "Shipped", LocalDateTime.now()
        );
        order.setId(1L);
        when(orderService.getOrderById(order.getId())).thenReturn(order);

        // Act & Assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/1"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Shipped"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.orderTime").exists()) 
            .andExpect(MockMvcResultMatchers.jsonPath("$.book.publishingDate").value("2024-07-29")); 
    }

    @Test
    void testCreateOrder() throws Exception {
        // Arrange
        Order order = new Order(
            new Book("Sample Book", "Sample Author", "1234567890", 19.99, "2024-07-29"),
            new Customer("John Doe", "123 Main St", "john.doe@example.com", "securePassword123"),
            "Shipped", LocalDateTime.now()
        );
        when(orderService.saveOrder(any(Order.class))).thenReturn(order);

        // Act & Assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"book\": {\"id\": 1, \"publishingDate\": \"2024-07-29\"},"
                    + " \"customer\": {\"id\": 1},"
                    + " \"status\": \"Shipped\","
                    + " \"orderTime\": \"2024-07-29T10:15:30\"}")) 
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Shipped"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.orderTime").exists()) 
            .andExpect(MockMvcResultMatchers.jsonPath("$.book.publishingDate").value("2024-07-29"));
        verify(orderService).saveOrder(any(Order.class));
    }

    @Test
    void testUpdateOrder() throws Exception {
        // Arrange
        Order existingOrder = new Order(
            new Book("Sample Book", "Sample Author", "1234567890", 19.99, "2023-01-01"),
            new Customer("John Doe", "123 Main St", "john.doe@example.com", "securePassword123"),
            "Shipped", LocalDateTime.now()
        );
        existingOrder.setId(1L);
        Order updatedOrder = new Order(
            new Book("Updated Book", "Updated Author", "0987654321", 29.99, "2024-07-29"),
            new Customer("Jane Smith", "456 Elm St", "jane.smith@example.com", "newPassword123"),
            "Delivered",
            LocalDateTime.now()
        );
        updatedOrder.setId(1L);
        when(orderService.updateOrder(any(Long.class), any(Order.class))).thenReturn(updatedOrder);

        // Act & Assert
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/orders/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"book\": {\"id\": 1, \"publishingDate\": \"2024-07-29\"},"
                    + " \"customer\": {\"id\": 1},"
                    + " \"status\": \"Delivered\","
                    + " \"orderTime\": \"2024-07-29T10:15:30\"}")) 
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("Delivered"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.orderTime").exists()) 
            .andExpect(MockMvcResultMatchers.jsonPath("$.book.publishingDate").value("2024-07-29")); 

        verify(orderService).updateOrder(any(Long.class), any(Order.class));
    }

    @Test
    void testDeleteOrder() throws Exception {
        // Act & Assert
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/orders/1"))
            .andExpect(MockMvcResultMatchers.status().isOk());

        verify(orderService).deleteOrder(1L);
    }
}

