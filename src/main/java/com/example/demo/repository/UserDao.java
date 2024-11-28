package com.example.demo.repository;

import java.util.Optional;

import com.example.demo.entity.User;

public interface UserDao {
	void save(User user);
	Optional<User> findByUsername(String username);
	boolean existByUsername(String username);         // 該用戶是否存在
}