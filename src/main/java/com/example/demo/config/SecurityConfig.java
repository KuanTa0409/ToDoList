package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration      // Spring配置類
@EnableWebSecurity  // 啟用Spring Security的Web安全功能
public class SecurityConfig {
    
    @Bean   // 讓Spring進行管理
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // URL訪問權限配置（使用新的authorizeHttpRequests方法 替代已棄用的authorizeRequests）
            .authorizeHttpRequests(auth -> auth
        		// 允許靜態資源訪問
                .requestMatchers("/images/**", "/css/**", "/js/**").permitAll()
                // 允許登入、註冊相關頁面
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/user/register/detail").permitAll()
                // 所有其他請求都 需要認證
                .anyRequest().authenticated()
            )
            
            // 登入配置
            .formLogin(form -> form
                .loginPage("/auth/login") // 自定義登入頁面的路徑
                // 登入表單 提交的處理路徑（預設是/login）
                .loginProcessingUrl("/auth/login")
                .defaultSuccessUrl("/user/index")      // 登入成功後 跳轉的頁面
                .failureUrl("/auth/login?error=true") // 登入失敗後 跳轉的頁面
                .permitAll()  //登入相關的頁面 可以被所有人訪問
            )
            
            // 登出配置
            .logout(logout -> logout
                // 設定登出的請求路徑
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/auth/login?logout=true") // 登出成功後 跳轉的頁面
                .clearAuthentication(true)   // 登出時 清除認證信息
                .invalidateHttpSession(true) // session失效
                .deleteCookies("JSESSIONID") // 清除cookie
                .permitAll() // 允許所有人訪問登出功能
            )
            
            // Session管理配置(防止同時多處登入)
            .sessionManagement(session -> session
                .maximumSessions(1) //限制同一用戶只能有一個活動session
                .expiredUrl("/login?expired=true") // session過期後的重定向url
            );
            
        return http.build();
    }
    
    /**
     * 密碼編碼器配置
     * BCryptPasswordEncoder是Spring Security提供的密碼編碼器，用於密碼的雜湊（加密）
     * - 它使用BCrypt強雜湊函數，專門設計用於密碼雜湊
     * - BCrypt自動處理加鹽（salt），不需要額外配置，不需單獨保存鹽值
     * - 參數10是工作因子，決定雜湊的強度，建議值為10-12之間
     *   - 值越大，雜湊越強，但耗時越長
     *   - 值為10代表進行2^10次雜湊計算
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }
}