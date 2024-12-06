package com.example.demo.entity;

public class Weather {
	private String location;     
	private String temperature; 
	private String humidity;    // 濕度
	private String description;
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "Weather [location=" + location + ", temperature=" + temperature + ", humidity=" + humidity
				+ ", description=" + description + "]";
	}
}