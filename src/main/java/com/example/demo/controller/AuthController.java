package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {
	
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	private UserService userService;
	
	// 登入頁面
	@GetMapping("/login")        
	public String loginPage() {  // URL(Get): /auth/login
		return "auth/login";
	}
	
	// 註冊(第一頁)
	@GetMapping("/register")
	public String registerPage(Model model) {  // URL(Get): /auth/register
		model.addAttribute("user", new User());
		return "auth/register";
	}
	
	// 註冊(第一頁)的表單提交
	@PostMapping("/register")                 // URL(Post): /auth/register
	public String processRegister(@Valid @ModelAttribute("user") User user, 
			                      @RequestParam("confirm") String confirm, 
			                      BindingResult result, HttpSession session) {
		log.info("用戶註冊: {}", user.getUsername());
		if(!user.getPassword().equals(confirm)) {
			result.rejectValue("password", "error.user", "密碼不相符");
			return "auth/register";
		}
		if(result.hasErrors()) {  // 有驗證錯誤，返回註冊頁面
			return "auth/register";
		}
		
		try {
			userService.register(user);
			session.setAttribute("registerUserId", user.getId()); //將用戶ID存入session，用於下一步填寫詳細資料
			log.info("用戶註冊成功，下一步請填寫詳細資料: {}", user.getUsername());
			return "redirect:/user/register/detail";
		} catch (RuntimeException e) {
			// 處理註冊失敗（例:用戶名已存在）
            result.rejectValue("username", "error.user", e.getMessage());
            log.warn("用戶註冊失敗: {}, 原因: {}", user.getUsername(), e.getMessage());
            return "auth/register";
		}
	}
	
	// 登出
	@PostMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();  // 清除session
		return "redirect:/auth/login?logout=true";
	}
}