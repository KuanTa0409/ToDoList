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

public class UserDetail {
		
	private Long uid;
	
	@NotBlank(message = "{NotBlank.userDetail.name}")
    @Size(max = 12, message = "{Size.userDetail.name}")
	private String name; //姓名
	
	@Min(value = 12, message = "{Min.userDetail.age}")
    @Max(value = 100, message = "{Max.userDetail.age}")
	private Integer age;
	
	@NotNull(message = "{NotNull.userDetail.birth}")
    @Past(message = "{Past.userDetail.birth}")
	private LocalDate birth;
	
	@NotBlank(message = "{NotBlank.userDetail.gender}")
	private String gender; //性別
	
	@NotBlank(message = "{NotBlank.userDetail.education}")
	private String education;
	
	@NotEmpty(message = "{NotEmpty.userDetail.interest}")
	private  List<String> interest;
	
	@Size(max = 150, message = "{Size.userDetail.resume}")
	private String resume;
	
	private LocalDateTime createtime;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public LocalDate getBirth() {
		return birth;
	}

	public void setBirth(LocalDate birth) {
		this.birth = birth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public List<String> getInterest() {
		return interest;
	}

	public void setInterest(List<String> interest) {
		this.interest = interest;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public LocalDateTime getCreatetime() {
		return createtime;
	}

	public void setCreatetime(LocalDateTime createtime) {
		this.createtime = createtime;
	}

	@Override
	public String toString() {
		return "UserDetail [uid=" + uid + ", name=" + name + ", age=" + age + ", birth=" + birth + ", gender=" + gender
				+ ", education=" + education + ", interest=" + interest + ", resume=" + resume + ", createtime="
				+ createtime + "]";
	}
}