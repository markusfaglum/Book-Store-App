package com.book.store.app.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.book.store.app.api.entity.Customer;
import com.book.store.app.api.repo.CustomerRepo;

@Service
public class CustomerService {
	
	@Autowired
    private CustomerRepo customerRepo;
    
    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }
    
    public Customer getCustomerById(Long id) {
    	return customerRepo.findById(id).orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
    }
    
    public Customer saveCustomer (Customer customer) {
		return customerRepo.save(customer);
	}
    
    public Customer updateCustomer(Long id, Customer customer) {
		Customer updatecustomer = customerRepo.findById(id).orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
		
		updatecustomer.setName(customer.getName());
		updatecustomer.setAddress(customer.getAddress());
		updatecustomer.setEmail(customer.getEmail());
		updatecustomer.setPassword(customer.getPassword());
		
		return customerRepo.save(updatecustomer);
	}
    
    
	public void deleteCustomer(Long id) {
		Customer deleteCustomer = customerRepo.findById(id).orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
		customerRepo.delete(deleteCustomer);
		
	}

}
