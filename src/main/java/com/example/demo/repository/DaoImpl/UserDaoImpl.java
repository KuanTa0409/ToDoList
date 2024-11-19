package com.example.demo.repository.DaoImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;
import com.example.demo.repository.UserDao;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private JdbcTemplate jdbctemplate;
	
	
	
	@Override
	public void save(User user) {
		String sql="INSERT INTO user(username, password) VALUES (?,?)";
		jdbctemplate.update(sql, user.);
		
	}

	@Override
	public Optional<User> findByUsername(String username) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public boolean existByUsername(String username) {
		// TODO Auto-generated method stub
		return false;
	}	
}