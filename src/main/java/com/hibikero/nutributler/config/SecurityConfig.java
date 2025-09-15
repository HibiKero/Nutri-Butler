package com.hibikero.nutributler.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security配置
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // 禁用CSRF，因为这是API服务
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/**").permitAll() // 允许所有API访问
                .requestMatchers("/static/**").permitAll() // 允许静态资源访问
                .requestMatchers("/").permitAll() // 允许根路径访问
                .requestMatchers("/vue-app.html").permitAll() // 允许前端页面访问
                .anyRequest().permitAll() // 暂时允许所有请求
            )
            .httpBasic(httpBasic -> httpBasic.disable()) // 禁用HTTP Basic认证
            .formLogin(formLogin -> formLogin.disable()); // 禁用表单登录

        return http.build();
    }
}
