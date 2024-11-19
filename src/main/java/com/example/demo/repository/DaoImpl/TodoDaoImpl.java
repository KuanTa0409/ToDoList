package com.example.demo.repository.DaoImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.demo.entity.Todo;
import com.example.demo.repository.TodoDao;

@Repository
public class TodoDaoImpl implements TodoDao {

	@Override
	public void save(Todo todo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Todo todo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Todo todo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Todo> findAllByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Todo> findById(Long tid) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}
}