package com.book.store.app.api.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.book.store.app.api.entity.Book;
import com.book.store.app.api.entity.Customer;
import com.book.store.app.api.entity.Order;
import com.book.store.app.api.repo.BookRepo;
import com.book.store.app.api.repo.CustomerRepo;
import com.book.store.app.api.repo.OrderRepo;

@Service
public class OrderService {

	@Autowired
    private OrderRepo orderRepo;
	
	@Autowired
    private BookRepo bookRepo; // Assuming you have a repository for Book
    @Autowired
    private CustomerRepo customerRepo; // Assuming you have a repository for Customer


    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepo.findById(id).orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }
    
    public Order saveOrder(Order order) {
        // Ensure that Book and Customer are set and valid
        Book book = bookRepo.findById(order.getBook().getId())
                            .orElseThrow(() -> new RuntimeException("Book not found"));
        Customer customer = customerRepo.findById(order.getCustomer().getId())
                                        .orElseThrow(() -> new RuntimeException("Customer not found"));
        order.setBook(book);
        order.setCustomer(customer);
        return orderRepo.save(order);
    }
    
 
    public Order updateOrder(Long id, Order order) {
    	Order updateOrder = orderRepo.findById(id).orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
 
    	updateOrder.setBook(order.getBook());
    	updateOrder.setCustomer(order.getCustomer());
    	updateOrder.setStatus(order.getStatus());
    	updateOrder.setOrderTime(order.getOrderTime());
            
            return orderRepo.save(updateOrder);
    }

    public void deleteOrder(Long id) {
    	Order deleteOrder = orderRepo.findById(id).orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    		 orderRepo.delete(deleteOrder);
    		 
    }
}
