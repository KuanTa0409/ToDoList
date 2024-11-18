package com.example.demo.repository;

import java.util.Optional;

import com.example.demo.entity.UserDetail;

public interface UserDetailDao {
	void save(UserDetail userDetail);
	void update(UserDetail userDetail);
	Optional<UserDetail> findById(Long uid);
}
