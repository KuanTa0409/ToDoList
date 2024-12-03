package com.example.demo.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Todo;
import com.example.demo.service.TodoService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/todos")
public class TodoController {
	
	@Autowired
	private TodoService todoService;
	
	private static final Logger log = LoggerFactory.getLogger(TodoController.class);
	
	// 首頁(查詢該用戶 所有待辦事項)
	@GetMapping("/")
	public String showList(Model model, Principal principal) {
		String username = principal.getName();
		log.debug("顯示用戶待辦事項: {}", username);
		
		List<Todo> todos = todoService.getUserTodos(username);
		model.addAttribute("username", username);
		model.addAttribute("todos", todos);
		return "todo/list";
	}
	
	// 查詢單一待辦事項
	@GetMapping("/{id}")
	public String get(Model model, Principal principal, 
			          @PathVariable("id")Long id,
			          @RequestParam(value = "action", required = false)String action) {
		try {
			Todo todo = todoService.getTodo(id);
			validateUserAccess(principal.getName(), todo);
			model.addAttribute("todo", todo);
			if("delete".equals(action)) {
				return "todo/todo_delete";
			}
			return "todo/todo_update";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
	        return "redirect:error";
		}
	}
	
	// 新增待辦
	@PostMapping("/")
	public String add(@Valid @ModelAttribute Todo todo, BindingResult result,
					  RedirectAttributes attr, Principal principal) {
		String username = principal.getName();
		log.debug("新增待辦事項: {}", username);
		if(result.hasErrors()) {
			return "todo/list";
		}
		try {
			
			todo.setTusername(username);
			todo.setCreatedAt(LocalDateTime.now());
	        todo.setUpdatedAt(LocalDateTime.now());
	        todo.setCompleted(false);
	        todoService.addTodo(todo);
	        
			attr.addFlashAttribute("message","新增成功");
			return "redirect:./";
		} catch (Exception e) {
			attr.addFlashAttribute("message", "新增失敗：" + e.getMessage());
	        return "redirect:error";
		}
	}
	
	// 修改
	@PutMapping("/{id}")
	public String update(@Valid @ModelAttribute Todo todo, BindingResult result,
			  			 RedirectAttributes attr, Principal principal,
			          	 @PathVariable("id") int id) {
		if(result.hasErrors()) {
			return "redirect:error";
		}
		List<Todo> todos = todoService.getUserTodos(principal.getName());
		todos.set(id, todo);
		// 將 todo 物件資料傳遞給 /updateOK，再傳給 success.html 顯示, 可以防止二次 submit
		attr.addFlashAttribute("todo", todo); 
		attr.addFlashAttribute("message","修改成功");
		return "redirect:updateOK"; 
	}
	
	// 刪除
	@DeleteMapping("/{id}")
	public String delete(@Valid @ModelAttribute Todo todo, RedirectAttributes attr,
						 @PathVariable("id") int id, Principal principal) {
		try {
			List<Todo> todos = todoService.getUserTodos(principal.getName());
			Todo removeTodo = todos.remove(id);
			attr.addFlashAttribute("removeTodo", removeTodo);
	        attr.addFlashAttribute("message", "刪除成功");
	        return "redirect:deleteOK";
		} catch (IndexOutOfBoundsException e) {
			attr.addFlashAttribute("message", "刪除資料錯誤：索引不存在");
	        return "redirect:error";
		} catch (Exception e) {
	        attr.addFlashAttribute("message", "刪除資料錯誤：" + e.getMessage());
	        return "redirect:error";
	    }
	}
	
	// 更新待辦事項狀態
	@PostMapping("/{id}/toggle")
    public String toggleStatus(@PathVariable("id") Long id,
                               Principal principal,RedirectAttributes attr) {
        try {
            Todo todo = todoService.getTodo(id);
            validateUserAccess(principal.getName(), todo);
            todo.setCompleted(!todo.isCompleted());
            todo.setUpdatedAt(LocalDateTime.now());
            todoService.updateTodo(todo);
            
            attr.addFlashAttribute("message", todo.isCompleted() ? "完成" : "未完成");
            return "redirect:/todos/";
            
        } catch (IndexOutOfBoundsException e) {
            attr.addFlashAttribute("message", "更新狀態錯誤：索引不存在");
            return "redirect:error";
        } catch (Exception e) {
            attr.addFlashAttribute("message", "更新狀態錯誤：" + e.getMessage());
            return "redirect:error";
        }
    }
	
	// 操作成功
	@GetMapping(value = { "/addOK", "/updateOK", "/deleteOK" })
	public String success() {
		return "success";
	}
	
	// 操作失敗
	@GetMapping(value = "/error")
	public String error() {
		return "error";
	}
	
	private void validateUserAccess(String username, Todo todo) {
	    if (!todo.getTusername().equals(username)) {
	        throw new RuntimeException("無權操作此待辦事項");
	    }
	}
}