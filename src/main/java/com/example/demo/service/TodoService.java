package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Todo;
import com.example.demo.repository.TodoDao;

@Service
public class TodoService {
	
	private static final Logger log = LoggerFactory.getLogger(TodoService.class);
	
	@Autowired
	private TodoDao todoDao;

	@Transactional
	public void addTodo(Todo todo) {
		log.info("Create new todo for user: {}", todo.getTusername());
        todoDao.save(todo);
        log.info("Created successfully!: {}", todo.getTusername());
	}
	
	@Transactional
    public void updateTodo(Todo todo) {
        log.info("Update todo for id: {}", todo.getTid());
        todoDao.update(todo);
        log.info("Updated successfully!: {}", todo.getTid());
    }
	
	@Transactional
    public void deleteTodo(Long tid) {
        log.info("Delete todo for id: {}", tid);
        todoDao.delete(tid);
        log.info("Deleted successfully!: {}", tid);
    }
	
	
}