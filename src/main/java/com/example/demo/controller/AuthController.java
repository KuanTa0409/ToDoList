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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController { // http://localhost:8081/TODO/auth/login

	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private UserService userService;

	// 登入頁面
	@GetMapping("/login")
	public String loginPage() { // URL(Get): /auth/login
		return "auth/login";
	}

	// 註冊(第一頁)
	@GetMapping("/register")
	public String registerPage(Model model) { // URL(Get): /auth/register
		model.addAttribute("user", new User());
		return "auth/register";
	}

	// 註冊(第一頁)的表單提交
	@PostMapping("/register") // URL(Post): /auth/register
	public String processRegister(@Valid @ModelAttribute("user") User user, BindingResult result,
			@RequestParam("confirm") String confirm, HttpSession session) {
		log.info("用戶註冊: {}", user.getUsername());
		// 先檢查驗證結果， 再比較密碼
		if (result.hasErrors()) { // 有驗證錯誤，返回註冊頁面
			return "auth/register";
		}
		if (!user.getPassword().equals(confirm)) {
			result.rejectValue("password", "error.user", "密碼不相符");
			return "auth/register";
		}

		try {
			userService.register(user);
			Long userId = user.getId();
			if (userId == null) {
				// 如果沒有 ID，嘗試重新查詢用戶
				User savedUser = userService.getUserByUsername(user.getUsername());
				userId = savedUser.getId();
			}

			session.setAttribute("registerUserId", userId); // 將用戶ID存入session，用於下一步填寫詳細資料
			log.info("用戶註冊成功，下一步請填寫詳細資料: {}", user.getUsername());
			return "redirect:/user/register/detail";
		} catch (RuntimeException e) {
			// 處理註冊失敗（例:用戶名已存在）
			result.rejectValue("username", "error.user", e.getMessage());
			log.warn("用戶註冊失敗: {}, 原因: {}", user.getUsername(), e.getMessage());
			return "auth/register";
		}
	}

	// 登入
	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password,
			RedirectAttributes redirectAttributes) {
		log.info("用戶登入: {}", username);

		try {
			// 檢查用戶名和密碼
			userService.pass(username, password);
			log.info("用戶登入成功: {}", username);
			return "redirect:/user/index";

		} catch (RuntimeException e) {
			log.warn("用戶登入失敗: {}, 原因: {}", username, e.getMessage());
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/auth/login?error=true";
		}
	}

	// 登出
	@PostMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); // 清除session
		return "redirect:/auth/login?logout=true";
	}
}