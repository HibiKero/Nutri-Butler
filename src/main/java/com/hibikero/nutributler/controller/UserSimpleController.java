package com.hibikero.nutributler.controller;

import com.hibikero.nutributler.common.Result;
import com.hibikero.nutributler.entity.UserSimple;
import com.hibikero.nutributler.service.UserSimpleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器（简化版）
 */
@RestController
@RequestMapping("/api/user-simple")
public class UserSimpleController {

    @Autowired
    private UserSimpleService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<UserSimple> register(@Valid @RequestBody UserSimple user) {
        return userService.register(user);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<UserSimple> login(@RequestBody LoginRequest request) {
        return userService.login(request.getUsername(), request.getPassword());
    }
    
    /**
     * 登录请求类
     */
    public static class LoginRequest {
        private String username;
        private String password;
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    /**
     * 根据ID获取用户
     */
    @GetMapping("/{id}")
    public Result<UserSimple> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * 根据用户名获取用户
     */
    @GetMapping("/username/{username}")
    public Result<UserSimple> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public Result<UserSimple> updateUser(@PathVariable Long id, @Valid @RequestBody UserSimple user) {
        user.setId(id);
        return userService.updateUser(user);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    /**
     * 获取所有用户
     */
    @GetMapping("/all")
    public Result<List<UserSimple>> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * 根据昵称搜索用户
     */
    @GetMapping("/search")
    public Result<List<UserSimple>> searchUsersByNickname(@RequestParam String nickname) {
        return userService.searchUsersByNickname(nickname);
    }
}