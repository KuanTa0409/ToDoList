package com.example.demo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.Weather;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class WeatherService {
	/*
	 * 中央氣象署開放資料平臺之資料擷取API
	 * https://opendata.cwa.gov.tw/dist/opendata-swagger.html
	 */
	private static final String WEATHER_API_URL = "https://opendata.cwa.gov.tw/dist/opendata-swagger.html";
	private final RestTemplate restTemplate;
	private final String authKey; // 從配置文件讀取API金鑰
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
    public WeatherService(RestTemplate restTemplate, @Value("${weather.api.key}") String authKey) {
        this.restTemplate = restTemplate;
        this.authKey = authKey;
    }
	
	public List<Weather> getWeatherInfo() {
        String url = WEATHER_API_URL + "?Authorization=" + authKey;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            // 解析JSON回應並轉換為Weather對象列表
            // 這裡需要根據實際API回應格式來調整解析邏輯
            return parseWeatherData(response.getBody());
        } catch (Exception e) {
            log.error("獲取天氣資訊失敗", e.getMessage());
            return Collections.emptyList();
        }
    }
	
	// 解析氣象局API回傳的JSON資料
    private List<Weather> parseWeatherData(String jsonData) {
        List<Weather> weatherList = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonData);
            
            // 取得天氣資料陣列
            JsonNode records = root.path("records");
            JsonNode locations = records.path("location");
            
            // 遍歷每個地區的資料
            for (JsonNode location : locations) {
                Weather weather = new Weather();
                weather.setLocation(location.path("locationName").asText());
                
                // 取得天氣現象
                JsonNode weatherElement = location.path("weatherElement");
                for (JsonNode element : weatherElement) {
                    String elementName = element.path("elementName").asText();
                    JsonNode time = element.path("time").get(0); // 取得最新時間的資料
                    JsonNode parameter = time.path("parameter");
                    
                    switch (elementName) {
                        case "Wx": // 天氣現象
                            weather.setDescription(parameter.path("parameterName").asText());
                            break;
                        case "PoP": // 降雨機率
                            weather.setHumidity(parameter.path("parameterName").asText() + "%");
                            break;
                        case "MinT": // 最低溫度
                            weather.setTemperature(parameter.path("parameterName").asText() + "°C");
                            break;
                    }
                }
                weatherList.add(weather);
            }
        } catch (Exception e) {
            log.error("解析天氣資料失敗", e);
        }
        return weatherList;
    }
}