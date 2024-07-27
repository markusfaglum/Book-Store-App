package com.book.store.app.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.book.store.app.api.entity.Book;
import com.book.store.app.api.service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {
	
	 @Autowired
	    private BookService bookService;

	    @GetMapping
	    public List<Book> getAllBooks() {
	        return bookService.getAllBooks();
	    }

	    @GetMapping("/{id}")
	    public Book getBookById(@PathVariable Long id) {
	        return bookService.getBookById(id);
	    }

	    @PostMapping
	    public Book createBook(@RequestBody @Valid Book book) {
	        return bookService.saveBook(book);
	    }

	    @PutMapping("/{id}")
	    public Book updateBook(@PathVariable Long id, @RequestBody @Valid Book book) {
	        return bookService.updateBook(id, book);
	    }

	    @DeleteMapping("/{id}")
	    public void deleteBook(@PathVariable Long id) {
	        bookService.deleteBook(id);
	    }
	}