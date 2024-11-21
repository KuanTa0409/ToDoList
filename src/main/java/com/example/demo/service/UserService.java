package com.example.demo.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.User;
import com.example.demo.repository.UserDao;

import lombok.extern.slf4j.Slf4j;

@Service
public class UserService {

	// 手動創建Logger實例
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder; //密碼加密
	
	@Transactional
	public void register(User user) {
		log.info("開始註冊新用戶: {}", user.getUsername());
		
		// 檢查用戶名 是否已存在
		if(userDao.existByUsername(user.getUsername())) {
			log.warn("用戶名已存在: {}", user.getUsername());
            throw new RuntimeException("用戶名已存在");
		}
		// 先加密 再存儲用戶資訊
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userDao.save(user);
		log.info("用戶註冊成功: {}", user.getUsername());
	}
	
	public User getUserByUsername(String username) {
		return userDao.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("用戶不存在!"));
	}
	
	public void pass(String username, String password) {
		User user = userDao.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("用戶不存在"));
		
		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new RuntimeException("密碼錯誤");
		}
	}
}