package com.example.demo.service;

import java.util.List;

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
		if(todo.getDescription() == null || todo.getDescription().trim().isEmpty()) {
			throw new RuntimeException("待辦事項，敘述不能為空");
		}
        todoDao.save(todo);
        log.info("Created successfully! user: {}", todo.getTusername());
	}
	
	// 獲取用戶 所有的待辦事項
	public List<Todo> getUserTodos(String username){
		log.debug("User Todo list，user: {}", username);
		return todoDao.findAllByUsername(username);
	}
	
	@Transactional
    public void updateTodo(Todo todo) {
        log.info("Update todo for id: {}", todo.getTid());
        Todo existtd = todoDao.findById(todo.getTid())
        		.orElseThrow(() -> new RuntimeException("待辦事項不存在"));
        if (!existtd.getTusername().equals(todo.getTusername())) {
            throw new RuntimeException("無權修改此待辦事項");
        }
        
        todoDao.update(todo);
        log.info("Updated successfully!: {}", todo.getTid());
    }
	
	@Transactional
    public void deleteTodo(Long tid) {
        log.info("Delete todo for id: {}", tid);
        
        Todo todo = todoDao.findById(tid)
        		.orElseThrow(() -> new RuntimeException("待辦事項不存在"));
        
        todoDao.delete(tid);
        log.info("Deleted successfully!: {}", tid);
    }
}