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
	
}