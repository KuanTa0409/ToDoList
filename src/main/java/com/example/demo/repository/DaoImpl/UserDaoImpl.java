package com.example.demo.repository.DaoImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;
import com.example.demo.repository.UserDao;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	// RowMapper用於將數據庫查詢結果映射到Java對象
	private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        // 從ResultSet中獲取數據並設置到User對象中
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        return user;
    };
	
	@Override
	public void save(User user) {
		String sql = "INSERT INTO user(username, password) VALUES (?, ?)";
		jdbcTemplate.update(sql, user.getUsername(), user.getPassword());
	}

	@Override
	public Optional<User> findByUsername(String username) {
		String sql = "SELECT * FROM user WHERE username = ?";
		try {
			//                       查詢單個對象    SQL語句、行映射器、查詢參數
			User user = jdbcTemplate.queryForObject(sql, userRowMapper, username);
			return Optional.ofNullable(user); // 將結果包裝在Optional中返回
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public boolean existByUsername(String username) {
		String sql = "SELECT COUNT(*) FROM user WHERE username=?";
		// queryForObject將結果轉換為Integer
		int count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count > 0;  // count>0 表示用戶名已存在
	}	
}