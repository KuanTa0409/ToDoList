package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.entity.User;
import com.example.demo.repository.UserDao;

@Service
public class UserService {

	// 手動創建Logger實例
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder; //密碼加密
	
	@Transactional(rollbackFor = Exception.class)
	public void register(User user) {
		log.info("開始註冊新用戶: {}", user.getUsername());
		
		try {
			// 檢查用戶名 是否已存在
			if(userDao.existByUsername(user.getUsername())) {
				log.warn("用戶名已存在: {}", user.getUsername());
	            throw new RuntimeException("用戶名已存在");
			}
			// 先加密 再存儲用戶資訊
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userDao.save(user);
			
			// 確保獲得 儲存後的ID
			User savedUser = userDao.findByUsername(user.getUsername())
		            .orElseThrow(() -> new RuntimeException("用戶儲存失敗"));
		    user.setId(savedUser.getId());
			
			log.info("用戶註冊成功: {}", user.getUsername());
	    } catch (Exception e) {
	        log.error("用戶註冊失敗", e);
	        throw new RuntimeException("註冊失敗:" + e.getMessage());
	    }
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