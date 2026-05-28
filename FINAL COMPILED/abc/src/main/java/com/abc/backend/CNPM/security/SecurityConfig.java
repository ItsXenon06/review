package com.abc.backend.CNPM.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Tắt CSRF để cho phép gửi dữ liệu từ Frontend/Postman
                .csrf(csrf -> csrf.disable())

                // 2. Cho phép truy cập tự do vào tất cả các trang
                // Bạn có thể chỉnh sửa lại sau này khi cần bảo mật hệ thống
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}