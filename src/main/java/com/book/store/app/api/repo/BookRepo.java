package com.book.store.app.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.book.store.app.api.entity.Book;

public interface BookRepo extends JpaRepository<Book, Long>{

}
