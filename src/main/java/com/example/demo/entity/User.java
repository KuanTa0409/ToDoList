package com.example.demo.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class User {
	
	private Long id;
	
	@NotBlank(message = "用戶名不能為空")
	@Size(min = 5, max = 50, message = "用戶名長度須在5-50之間")
	@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "用戶名只能包含大小寫字母和數字")
	private String username; //使用者帳號
	
	@NotBlank(message = "密碼不能為空")
	@Size(min = 6, max = 20, message = "密碼長度須在6-20之間")
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