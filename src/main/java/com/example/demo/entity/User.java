package com.example.demo.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class User {
	
	private Long id;
	
	@NotBlank(message = "{NotBlank.user.username}")
	@Size(min = 5, max = 50, message = "{Size.user.username}")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "{Pattern.user.username}")
	private String username; //使用者帳號
	
	@NotBlank(message = "{NotBlank.user.password}")
	@Size(min = 6, max = 20, message = "{Size.user.password}")
	private String password; //使用者密碼
	
	private Todo todo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Todo getTodo() {
		return todo;
	}

	public void setTodo(Todo todo) {
		this.todo = todo;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", todo=" + todo + "]";
	}
}