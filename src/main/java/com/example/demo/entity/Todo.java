package com.example.demo.entity;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;

public class Todo {

	private Long tid;
	
	private String tusername;
	
	private String description;
	
	private boolean completed;
	
	private LocalDateTime createdAt; //自動設置 創建時間
	
	private LocalDateTime updatedAt; //自動 更新時間
	
	//多(Todo)對一(User)，多方維護
	@NotNull(message = "用戶不能為空")
	private User user;

	public Long getTid() {
		return tid;
	}

	public void setTid(Long tid) {
		this.tid = tid;
	}

	public String getTusername() {
		return tusername;
	}

	public void setTusername(String tusername) {
		this.tusername = tusername;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Todo [tid=" + tid + ", tusername=" + tusername + ", description=" + description + ", completed="
				+ completed + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", user=" + user + "]";
	}	
}