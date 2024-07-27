package com.book.store.app.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.book.store.app.api.entity.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long> {

}
