package com.example.demo.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Todo {

	private Long tid;
	
	// 同一個用戶可以建立多個待辦事項
	private String tusername;
	
	private String description;
	
	private boolean completed;
	
	private LocalDateTime createdAt; //自動設置 創建時間
	
	private LocalDateTime updatedAt; //自動 更新時間
	
	//多(Todo)對一(User)，多方維護)
	private User user;	
}