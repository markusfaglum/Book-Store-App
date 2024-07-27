package com.book.store.app.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.book.store.app.api.entity.Book;
import com.book.store.app.api.repo.BookRepo;

@Service
public class BookService {
    
	@Autowired
    private BookRepo bookRepo;
    
    public List<Book> getAllBooks() {
        return bookRepo.findAll();
    }
    
    public Book getBookById(Long id) {
    	return bookRepo.findById(id).orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }
    
    public Book saveBook (Book book) {
		return bookRepo.save(book);
	}
    
    public Book updateBook(Long id,Book book) {
		Book updateBook = bookRepo.findById(id).orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
		
		updateBook.setTitle(book.getTitle());
		updateBook.setAuthor(book.getAuthor());
		updateBook.setEan(book.getEan());
		updateBook.setPrice(book.getPrice());
		
		return bookRepo.save(updateBook);
	}
    
   
	public void deleteBook(Long id) {
		Book deleteBook = bookRepo.findById(id).orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
		bookRepo.delete(deleteBook);
		
	}
}
