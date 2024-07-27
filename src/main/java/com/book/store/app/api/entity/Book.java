package com.book.store.app.api.entity;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false)
	@NotBlank(message = "Name is blank")
	private String title;
	
	@Column(nullable = false)
	@NotBlank(message = "Author is blank")
	private String author;
	
	@Column(nullable = false)
	@NotBlank(message = "Ean is blank")
	private String ean;
	
	@Column(nullable = false)
	@Positive(message = "Price is negative")
	private double price;
	
	@Column(nullable = false)
	@NotNull(message = "Publishing date is null")
	private LocalDate publishingDate;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getEan() {
		return ean;
	}
	public void setEan(String ean) {
		this.ean = ean;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public LocalDate getPublishingDate() {
		return publishingDate;
	}
	public void setPublishingDate(LocalDate publishingDate) {
		this.publishingDate = publishingDate;
	}
	

}
