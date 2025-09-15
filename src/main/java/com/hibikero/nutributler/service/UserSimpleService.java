package com.hibikero.nutributler.service;

import com.hibikero.nutributler.entity.UserSimple;
import com.hibikero.nutributler.common.Result;

import java.util.List;

/**
 * 用户服务接口（简化版）
 */
public interface UserSimpleService {

    /**
     * 用户注册
     */
    Result<UserSimple> register(UserSimple user);

    /**
     * 用户登录
     */
    Result<UserSimple> login(String username, String password);

    /**
     * 根据ID获取用户
     */
    Result<UserSimple> getUserById(Long id);

    /**
     * 根据用户名获取用户
     */
    Result<UserSimple> getUserByUsername(String username);

    /**
     * 更新用户信息
     */
    Result<UserSimple> updateUser(UserSimple user);

    /**
     * 删除用户
     */
    Result<Void> deleteUser(Long id);

    /**
     * 获取所有用户
     */
    Result<List<UserSimple>> getAllUsers();

    /**
     * 根据昵称搜索用户
     */
    Result<List<UserSimple>> searchUsersByNickname(String nickname);
}