package com.example.demo.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.entity.UserDetail;
import com.example.demo.repository.UserDetailDao;

@Service
public class UserDetailService {

	private static final Logger log = LoggerFactory.getLogger(UserDetailService.class);
	
	@Autowired
	private UserDetailDao userDetailDao;
	
	@Transactional
	public void saveUserDetail(UserDetail userDetail) {
		log.info("保存用戶詳細資料: {}", userDetail.getUid());
		userDetailDao.save(userDetail);
		log.info("保存成功!: {}", userDetail.getUid());
	}
	
	@Transactional(readOnly = true)  
	public UserDetail getUserDetail(Long uid) {
		return userDetailDao.findById(uid)
				.orElseThrow(() -> new RuntimeException("用戶資料不存在!"));
	}
	
	@Transactional
	public void updateUserDetail(UserDetail userDetail) {
		log.info("Update user detail for uid: {}", userDetail.getUid());
		
		// 檢查用戶資料是否存在
        Optional<UserDetail> existDetail = userDetailDao.findById(userDetail.getUid());
        if (existDetail.isPresent()) {
            userDetailDao.update(userDetail); // 存在 > 更新
        } else {
            userDetailDao.save(userDetail); // 不存在 > 保存
        }
		
		log.info("updated successfully!: {}", userDetail.getUid());
	}
}