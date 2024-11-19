package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Todo;

public interface TodoDao {
	void save(Todo todo);
	void update(Todo todo);
	void delete(Long tid);
	List<Todo> findAllByUsername(String username);
	Optional<Todo> findById(Long tid);
}
