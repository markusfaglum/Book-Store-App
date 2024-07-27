package com.book.store.app.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.book.store.app.api.entity.Order;

public interface OrderRepo extends JpaRepository<Order, Long>{

}
