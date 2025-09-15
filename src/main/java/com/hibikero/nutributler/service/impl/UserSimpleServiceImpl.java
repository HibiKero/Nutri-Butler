package com.hibikero.nutributler.service.impl;

import com.hibikero.nutributler.common.Result;
import com.hibikero.nutributler.entity.UserSimple;
import com.hibikero.nutributler.repository.UserSimpleRepository;
import com.hibikero.nutributler.service.UserSimpleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务实现类（简化版）
 */
@Service
public class UserSimpleServiceImpl implements UserSimpleService {

    @Autowired
    private UserSimpleRepository userRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Result<UserSimple> register(UserSimple user) {
        try {
            // 验证必填字段
            if (!StringUtils.hasText(user.getUsername())) {
                return Result.error("用户名不能为空");
            }
            if (!StringUtils.hasText(user.getPassword())) {
                return Result.error("密码不能为空");
            }
            if (!StringUtils.hasText(user.getEmail())) {
                return Result.error("邮箱不能为空");
            }
            
            // 检查用户名是否已存在
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                return Result.error("用户名已存在");
            }

            // 检查邮箱是否已存在
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return Result.error("邮箱已存在");
            }

            // 检查手机号是否已存在（如果提供了手机号）
            if (StringUtils.hasText(user.getPhone()) && 
                userRepository.findByPhone(user.getPhone()).isPresent()) {
                return Result.error("手机号已存在");
            }

            // 设置默认值
            if (user.getStatus() == null) {
                user.setStatus(1);
            }
            if (user.getDeleted() == null) {
                user.setDeleted(0);
            }
            
            // 加密密码
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // 保存用户
            UserSimple savedUser = userRepository.save(user);
            return Result.success(savedUser);
        } catch (Exception e) {
            return Result.error("注册失败：" + e.getMessage());
        }
    }

    @Override
    public Result<UserSimple> login(String username, String password) {
        try {
            // 首先检查用户是否存在
            Optional<UserSimple> userOpt = userRepository.findByUsername(username);
            if (!userOpt.isPresent()) {
                return Result.error("用户不存在");
            }
            
            UserSimple user = userOpt.get();
            
            // 检查用户是否被删除
            if (user.getDeleted() == 1) {
                return Result.error("用户不存在");
            }
            
            // 检查用户状态
            if (user.getStatus() == 0) {
                return Result.error("用户已被禁用");
            }
            
            // 验证密码
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return Result.error("密码错误");
            }
            
            return Result.success(user);
        } catch (Exception e) {
            return Result.error("登录失败：" + e.getMessage());
        }
    }

    @Override
    public Result<UserSimple> getUserById(Long id) {
        try {
            Optional<UserSimple> userOpt = userRepository.findById(id);
            if (userOpt.isPresent()) {
                UserSimple user = userOpt.get();
                if (user.getDeleted() == 1) {
                    return Result.error("用户不存在");
                }
                return Result.success(user);
            } else {
                return Result.error("用户不存在");
            }
        } catch (Exception e) {
            return Result.error("获取用户信息失败：" + e.getMessage());
        }
    }

    @Override
    public Result<UserSimple> getUserByUsername(String username) {
        try {
            Optional<UserSimple> userOpt = userRepository.findByUsername(username);
            if (userOpt.isPresent()) {
                UserSimple user = userOpt.get();
                if (user.getDeleted() == 1) {
                    return Result.error("用户不存在");
                }
                return Result.success(user);
            } else {
                return Result.error("用户不存在");
            }
        } catch (Exception e) {
            return Result.error("获取用户信息失败：" + e.getMessage());
        }
    }

    @Override
    public Result<UserSimple> updateUser(UserSimple user) {
        try {
            if (user.getId() == null) {
                return Result.error("用户ID不能为空");
            }

            Optional<UserSimple> existingUserOpt = userRepository.findById(user.getId());
            if (!existingUserOpt.isPresent()) {
                return Result.error("用户不存在");
            }

            UserSimple existingUser = existingUserOpt.get();
            if (existingUser.getDeleted() == 1) {
                return Result.error("用户不存在");
            }

            // 更新用户信息
            UserSimple updatedUser = userRepository.save(user);
            return Result.success(updatedUser);
        } catch (Exception e) {
            return Result.error("更新用户信息失败：" + e.getMessage());
        }
    }

    @Override
    public Result<Void> deleteUser(Long id) {
        try {
            Optional<UserSimple> userOpt = userRepository.findById(id);
            if (!userOpt.isPresent()) {
                return Result.error("用户不存在");
            }

            UserSimple user = userOpt.get();
            if (user.getDeleted() == 1) {
                return Result.error("用户不存在");
            }

            // 软删除
            user.setDeleted(1);
            userRepository.save(user);
            return Result.success();
        } catch (Exception e) {
            return Result.error("删除用户失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<UserSimple>> getAllUsers() {
        try {
            List<UserSimple> users = userRepository.findAllActiveUsers();
            return Result.success(users);
        } catch (Exception e) {
            return Result.error("获取用户列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<UserSimple>> searchUsersByNickname(String nickname) {
        try {
            if (!StringUtils.hasText(nickname)) {
                return Result.error("昵称不能为空");
            }
            List<UserSimple> users = userRepository.findByNicknameContaining(nickname);
            return Result.success(users);
        } catch (Exception e) {
            return Result.error("搜索用户失败：" + e.getMessage());
        }
    }
}