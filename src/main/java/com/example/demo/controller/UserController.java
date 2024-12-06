package com.example.demo.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

import com.example.demo.entity.User;
import com.example.demo.entity.UserDetail;
import com.example.demo.entity.Weather;
import com.example.demo.service.UserDetailService;
import com.example.demo.service.UserService;
import com.example.demo.service.WeatherService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDetailService userDetailService;
	
	@Autowired
	private WeatherService weatherService;
	
	// 主頁
	@GetMapping("/index")   // URL(Get): /user/index
	public String index(Model model, Principal principal) {
		String username = principal.getName();
		log.debug("顯示用戶主頁: {}", username);
		
		User user = userService.getUserByUsername(username);
		model.addAttribute("user", user);
		UserDetail userDetail = userDetailService.getUserDetail(user.getId());
		model.addAttribute("userDetail", userDetail);
		
		// 氣候資訊
		List<Weather> weatherInfo = weatherService.getWeatherInfo();
		model.addAttribute("weatherInfo", weatherInfo);
		return "user/index";
	}
	
	// 註冊(第二頁)
	@GetMapping("/register/detail")   // URL(Get): /user/register/detail
	public String registerDetailPage(Model model, HttpSession session) {
		Long userId = (Long)session.getAttribute("registerUserId"); //從session取得註冊的用戶ID
		
		if(userId == null) {
			log.warn("找不到註冊用戶ID，請重新註冊");
			return "redirect:/auth/register";
		}
		
		UserDetail userDetail = new UserDetail();
		userDetail.setUid(userId);
		userDetail.setInterest(new ArrayList<>()); // 初始化興趣列表
		userDetail.setCreatetime(LocalDateTime.now());
		model.addAttribute("userDetail", userDetail);
		return "user/register_detail";
	}
	
	// 註冊(第二頁)的表單提交
	@PostMapping("/register/detail")   // URL(Post): /user/register/detail
	public String processRegisterDetail(@Valid @ModelAttribute("userDetail")UserDetail userDetail,
			                            BindingResult result, HttpSession session) {
		log.info("用戶詳細資料: {}", userDetail.getUid());
		if(result.hasErrors()) {
			return "user/register_detail";
		}
		
		try {
			userDetailService.saveUserDetail(userDetail);
			session.removeAttribute("registerUserId");
			log.info("用戶詳細資料註冊成功: {}", userDetail.getUid());
			return "redirect:/auth/login?register=success";
		} catch (Exception e) {
			result.rejectValue("name", "error.userDetail", e.getMessage());
			log.error("用戶詳細資料註冊失敗: {}, 原因: {}", userDetail.getUid(), e.getMessage());
			return "user/register_detail";
		}
	}
	
	@GetMapping("/profile")
	public String viewProfile(Model model, Principal principal) {
		String username = principal.getName();
		User user = userService.getUserByUsername(username);
		UserDetail userDetail = userDetailService.getUserDetail(user.getId());
		model.addAttribute("userDetail", userDetail);
		return "user/profile";
	}
	
	// 登入後，顯示個人資料
	@GetMapping("/profile/edit")   // URL(Get): /user/profile/edit
	public String editProfile(Model model, Principal principal) {
		String username = principal.getName();
		log.debug("編輯個人基本資料: {}", username);
		
		User user = userService.getUserByUsername(username);
		UserDetail userDetail = userDetailService.getUserDetail(user.getId());
		model.addAttribute("userDetail", userDetail);
		return "user/edit";
	}
	
	// 登入後，修改個人資料
	@PostMapping("/profile/edit")   // URL(Post): /user/profile/edit
    public String processEditProfile(
            @Valid @ModelAttribute("userDetail") UserDetail userDetail,
            BindingResult result) {
        
        log.info("用戶資料更新: {}", userDetail.getUid());
        
        if (result.hasErrors()) {
            return "user/edit";
        }
        
        try {
            userDetailService.updateUserDetail(userDetail);
            log.info("用戶資料更新成功: {}", userDetail.getUid());
            return "redirect:/user/index?update=success";
            
        } catch (Exception e) {
            result.rejectValue("name", "error.userDetail", e.getMessage());
            log.error("用戶資料更新失敗: {}, 原因: {}", userDetail.getUid(), e.getMessage());
            return "user/edit";
        }
    }
}