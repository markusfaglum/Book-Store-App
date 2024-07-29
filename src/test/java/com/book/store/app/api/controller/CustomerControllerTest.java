package com.book.store.app.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.book.store.app.api.entity.Customer;
import com.book.store.app.api.service.CustomerService;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    void testGetAllCustomers() throws Exception {
        // Arrange
        List<Customer> customers = List.of(
            new Customer("John Doe", "123 Main St", "john.doe@example.com", "securePassword123")
        );
        when(customerService.getAllCustomers()).thenReturn(customers);

        // Act & Assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/customers/getall"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("John Doe"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].address").value("123 Main St"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("john.doe@example.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].password").value("securePassword123"));
    }

    @Test
    void testGetCustomerById() throws Exception {
        // Arrange
        Customer customer = new Customer("John Doe", "123 Main St", "john.doe@example.com", "securePassword123");
        customer.setId(1L);
        when(customerService.getCustomerById(customer.getId())).thenReturn(customer);

        // Act & Assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/customers/1"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("123 Main St"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("john.doe@example.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("securePassword123"));
    }

    @Test
    void testCreateCustomer() throws Exception {
        // Arrange
        Customer customer = new Customer("Jane Doe", "456 Elm St", "jane.doe@example.com", "anotherPassword123");
        when(customerService.saveCustomer(any(Customer.class))).thenReturn(customer);

        // Act & Assert
        this.mockMvc.perform(MockMvcRequestBuilders.post("/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"Jane Doe\","
                    + " \"address\": \"456 Elm St\","
                    + " \"email\": \"jane.doe@example.com\","
                    + " \"password\": \"anotherPassword123\"}"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Jane Doe"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("456 Elm St"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("jane.doe@example.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("anotherPassword123"));
        
        verify(customerService).saveCustomer(any(Customer.class));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        // Arrange
        Customer existingCustomer = new Customer("John Doe", "123 Main St", "john.doe@example.com", "securePassword123");
        existingCustomer.setId(1L);
        Customer updatedCustomer = new Customer("Jane Smith", "789 Oak St", "jane.smith@example.com", "newPassword123");
        updatedCustomer.setId(1L);
        when(customerService.updateCustomer(any(Long.class), any(Customer.class))).thenReturn(updatedCustomer);

        // Act & Assert
        this.mockMvc.perform(MockMvcRequestBuilders.put("/customers/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\": \"Jane Smith\","
                    + " \"address\": \"789 Oak St\","
                    + " \"email\": \"jane.smith@example.com\","
                    + " \"password\": \"newPassword123\"}"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Jane Smith"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("789 Oak St"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("jane.smith@example.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("newPassword123"));
        
        verify(customerService).updateCustomer(any(Long.class), any(Customer.class));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        // Act & Assert
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/customers/1"))
            .andExpect(MockMvcResultMatchers.status().isOk());

        verify(customerService).deleteCustomer(1L);
    }
}

