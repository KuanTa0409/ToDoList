package com.example.demo.repository.DaoImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.Todo;
import com.example.demo.repository.TodoDao;

@Repository
public class TodoDaoImpl implements TodoDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/*
	 * 將數據庫查詢結果映射到Java對象
	 * 定義如何將ResultSet的每一行轉換為對應的對象
	 * 可以重用，提高程式複用性
	 */
	private final RowMapper<Todo> todoRowMapper = (rs, rowNum) -> {
        Todo todo = new Todo();
        todo.setTid(rs.getLong("tid"));
        todo.setTusername(rs.getString("tusername"));
        todo.setDescription(rs.getString("description"));
        todo.setCompleted(rs.getBoolean("completed"));
        todo.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime()); // 將TIMESTAMP轉換為LocalDateTime
        todo.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return todo;
    };
	
	@Override
	public void save(Todo todo) {
		String sql = "INSERT INTO todo (tusername, description, completed, created_at, updated_at, user_id) VALUES (?, ?, ?, ?, ?, ?)";
		
		jdbcTemplate.update(sql, 
		        todo.getTusername(),
		        todo.getDescription(),
		        todo.isCompleted(),
		        todo.getCreatedAt(),
		        todo.getUpdatedAt(),
		        todo.getUser().getId() // 該用戶的User.id
		    );
	}

	@Override
	public void update(Todo todo) {
		String sql = "UPDATE todo SET description = ?, completed = ?, updated_at = ? WHERE tid = ?";
        jdbcTemplate.update(sql,
            todo.getDescription(),
            todo.isCompleted(),
            LocalDateTime.now(),
            todo.getTid()
        );
	}

	@Override
	public void delete(Long tid) {
		String sql = "DELETE FROM todo WHERE tid = ?";
        jdbcTemplate.update(sql, tid);
    }

	@Override
	public List<Todo> findAllByUsername(String username) {
		// 查詢指定用戶的所有待辦事項，按建立時間降序排序
        String sql = "SELECT * FROM todo WHERE tusername = ? ORDER BY created_at DESC";
        // query方法用於返回多個結果
        return jdbcTemplate.query(sql, todoRowMapper, username);
	}

	@Override
	public Optional<Todo> findById(Long tid) {
		String sql = "SELECT * FROM todo WHERE tid = ?";
        try {
            Todo todo = jdbcTemplate.queryForObject(sql, todoRowMapper, tid);
            return Optional.ofNullable(todo);
        } catch (Exception e) {
            return Optional.empty();
            // 優雅地處理空值情況，避免NullPointerException
        }
	}
}