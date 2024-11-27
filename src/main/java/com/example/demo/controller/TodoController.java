package com.example.demo.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

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

import jakarta.validation.Valid;

@Controller
@RequestMapping("/todos")
public class TodoController {
	
	@Autowired
	private TodoService todoService;
	
	// 首頁(查詢該用戶 所有待辦事項)
	@GetMapping("/")
	public String index(Model model,Principal principal) {
		List<Todo> todos = todoService.getUserTodos(principal.getName());
		model.addAttribute("todos", todos);
		return "todo";
	}
	
	// 查詢單一待辦事項
	@GetMapping("/{index}")
	public String get(Model model, Principal principal, 
			          @PathVariable("index")int index,
			          @RequestParam(value = "action", required = false)String action) {
		List<Todo> todos = todoService.getUserTodos(principal.getName());
		Todo todo = todos.get(index);
		model.addAttribute("index", index);
		model.addAttribute("todo", todo);
		if(action != null && action.equals("delete")) {
			return "todo_delete";
		}
		return "todo_update";
	}
	
	// 新增待辦
	@PostMapping("/")
	public String add(@Valid @ModelAttribute Todo todo, RedirectAttributes attr,
			          BindingResult result, Principal principal) {
		if(result.hasErrors()) {
			return "redirect:error";
		}
		List<Todo> todos = todoService.getUserTodos(principal.getName());
		todos.add(todo);
		// 將 todo 物件資料傳遞給 /addOK，再傳給 success.html 顯示, 可以防止二次 submit
		attr.addFlashAttribute("todo", todo); // 按 重新整理，也不會二次輸入
		attr.addFlashAttribute("message","新增成功");
		return "redirect:addOK"; 
	}
	
	// 修改
	@PutMapping("/{index}")
	public String update(@Valid @ModelAttribute Todo todo, RedirectAttributes attr,
			          	BindingResult result, Principal principal,
			          	@PathVariable("index") int index) {
		if(result.hasErrors()) {
			return "redirect:error";
		}
		List<Todo> todos = todoService.getUserTodos(principal.getName());
		todos.set(index, todo);
		// 將 todo 物件資料傳遞給 /updateOK，再傳給 success.html 顯示, 可以防止二次 submit
		attr.addFlashAttribute("todo", todo); 
		attr.addFlashAttribute("message","修改成功");
		return "redirect:updateOK"; 
	}
	
	// 刪除
	@DeleteMapping("/{index}")
	public String delete(@Valid @ModelAttribute Todo todo, RedirectAttributes attr,
						 @PathVariable("index") int index, Principal principal) {
		try {
			List<Todo> todos = todoService.getUserTodos(principal.getName());
			Todo removeTodo = todos.remove(index);
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
	@PostMapping("/{index}/toggle")
    public String toggleStatus(@PathVariable("index") int index,
                               Principal principal,RedirectAttributes attr) {
        try {
            // 1. 獲取該用戶的所有待辦事項
            List<Todo> todos = todoService.getUserTodos(principal.getName());
        
            // 2. 獲取並更新指定待辦事項的狀態
            Todo todo = todos.get(index);
            todo.setCompleted(!todo.isCompleted());
            todo.setUpdatedAt(LocalDateTime.now());
            
            // 3. 更新待辦事項
            todos.set(index, todo);
            
            // 4. 添加成功訊息
            attr.addFlashAttribute("message", todo.isCompleted() ? "完成" : "未完成");
            
            // 5. 返回待辦事項列表頁面
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
}