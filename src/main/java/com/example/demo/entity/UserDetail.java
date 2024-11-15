package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDetail {
		
	private Long uid;
	
	@NotBlank(message = "姓名不能為空")
    @Size(max = 12, message = "姓名長度不能超過12個字符")
	private String name; //姓名
	
	@Min(value = 12, message = "年齡必須大於等於12歲")
    @Max(value = 100, message = "年齡必須小於等於100歲")
	private Integer age;
	
	@NotNull(message = "出生日期不能為空")
    @Past(message = "出生日期須是過去的日期")
	private LocalDate birth;
	
	@NotBlank(message = "性別不能為空")
	private String gender; //性別
	
	@NotBlank(message = "教育程度不能為空")
	private String education;
	
	@NotEmpty(message = "興趣不能為空")
	private  List<String> interest;
	
	@Size(max = 500, message = "簡歷長度不能超過500個字符")
	private String resume;
	
	private LocalDateTime createtime;
	
}