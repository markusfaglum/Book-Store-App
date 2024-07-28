package com.book.store.app.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false)
	@NotBlank(message = "Customer Name is blank")
	private String name;
	
	@Column(nullable = false)
	@NotBlank(message = "Customer address is blank")
	private String address;
	
	@Column(nullable = false)
	@NotBlank(message = "Customer email is blank")
	private String email;
	
	@Column(nullable = false)
	@NotBlank(message = "Customer password is blank")
	@Size(min = 6, max = 36, message = "Password must be between 6-36 characters")
	private String password;
	
	public Customer(Long id, String name, String address, String email, String password) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.password = password;
    }
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
