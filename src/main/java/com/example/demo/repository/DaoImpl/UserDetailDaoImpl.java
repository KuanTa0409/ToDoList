package com.example.demo.repository.DaoImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.UserDetail;
import com.example.demo.repository.UserDetailDao;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class UserDetailDaoImpl implements UserDetailDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// ObjectMapper用於JSON和Java對象之間的轉換
	@Autowired
	private ObjectMapper objectMapper;

	// 將數據庫結果映射到UserDetail對象
	private final RowMapper<UserDetail> userDetailRowMapper = (rs, rowNum) -> {
		UserDetail detail = new UserDetail();
		detail.setUid(rs.getLong("uid"));
		detail.setName(rs.getString("name"));
		detail.setAge(rs.getInt("age"));
		detail.setBirth(rs.getDate("birth").toLocalDate()); // 將DATE類型轉換為LocalDate
		detail.setGender(rs.getString("gender"));
		detail.setEducation(rs.getString("education"));

		// 將興趣(JSON格式)轉換為List<String>
		try {
			String interestJson = rs.getString("interest");
			List<String> interests = objectMapper.readValue(interestJson, new TypeReference<List<String>>() {
			});
			detail.setInterest(interests);
		} catch (Exception e) {
			throw new RuntimeException("Error parsing interest JSON", e);
		}
		detail.setResume(rs.getString("resume"));
		detail.setCreatetime(rs.getTimestamp("createtime").toLocalDateTime()); // 將TIMESTAMP轉換為LocalDateTime
		return detail;
	};

	@Override
	public void save(UserDetail userDetail) {
		String sql = "INSERT INTO user_detail (uid, name, age, birth, gender, "
				+ "education, interest, resume) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		// 將List<String>轉換為JSON字符串
		String interestJson;
		try {
			interestJson = objectMapper.writeValueAsString(userDetail.getInterest());
		} catch (Exception e) {
			throw new RuntimeException("Error converting interests to JSON", e);
		}

		jdbcTemplate.update(sql, userDetail.getUid(), userDetail.getName(), userDetail.getAge(), userDetail.getBirth(),
				userDetail.getGender(), userDetail.getEducation(), interestJson, userDetail.getResume());
	}

	@Override
	public void update(UserDetail userDetail) {
		String sql = "UPDATE user_detail SET name = ?, age = ?, birth = ?, "
				+ "gender = ?, education = ?, interest = ?, resume = ? WHERE uid = ?";

		String interestJson;
		try {
			interestJson = objectMapper.writeValueAsString(userDetail.getInterest());
		} catch (Exception e) {
			throw new RuntimeException("Error converting interests to JSON", e);
		}

		jdbcTemplate.update(sql, userDetail.getName(), userDetail.getAge(), userDetail.getBirth(),
				userDetail.getGender(), userDetail.getEducation(), interestJson, userDetail.getResume(),
				userDetail.getUid());
	}

	@Override
	public Optional<UserDetail> findById(Long uid) {
		String sql = "SELECT * FROM user_detail WHERE uid = ?";
		try {
			UserDetail detail = jdbcTemplate.queryForObject(sql, userDetailRowMapper, uid);
			return Optional.ofNullable(detail);
		} catch (Exception e) {
			return Optional.empty();
		}
	}
}