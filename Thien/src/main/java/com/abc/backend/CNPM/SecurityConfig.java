//package com.abc.backend.CNPM;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable) // Vô hiệu hóa CSRF theo chuẩn mới nhất
//                .cors(AbstractHttpConfigurer::disable) // Vô hiệu hóa CORS nếu test API tự do
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll() // Mở khóa toàn bộ request
//                );
//
//        return http.build();
//    }
//}