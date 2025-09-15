package com.hibikero.nutributler.controller;

import com.hibikero.nutributler.common.Result;
import com.hibikero.nutributler.entity.UserHealthProfile;
import com.hibikero.nutributler.service.UserHealthProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户健康档案控制器
 */
@RestController
@RequestMapping("/api/health-profile")
public class UserHealthProfileController {
    
    @Autowired
    private UserHealthProfileService healthProfileService;
    
    /**
     * 创建健康档案
     */
    @PostMapping
    public Result<UserHealthProfile> createHealthProfile(@Valid @RequestBody UserHealthProfile healthProfile) {
        return healthProfileService.createHealthProfile(healthProfile);
    }
    
    /**
     * 更新健康档案
     */
    @PutMapping("/{id}")
    public Result<UserHealthProfile> updateHealthProfile(@PathVariable Long id, @Valid @RequestBody UserHealthProfile healthProfile) {
        healthProfile.setId(id);
        return healthProfileService.updateHealthProfile(healthProfile);
    }
    
    /**
     * 根据用户ID获取健康档案
     */
    @GetMapping("/user/{userId}")
    public Result<UserHealthProfile> getHealthProfileByUserId(@PathVariable Long userId) {
        return healthProfileService.getHealthProfileByUserId(userId);
    }
    
    /**
     * 根据ID获取健康档案
     */
    @GetMapping("/{id}")
    public Result<UserHealthProfile> getHealthProfileById(@PathVariable Long id) {
        return healthProfileService.getHealthProfileById(id);
    }
    
    /**
     * 删除健康档案
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteHealthProfile(@PathVariable Long id) {
        return healthProfileService.deleteHealthProfile(id);
    }
    
    /**
     * 计算目标卡路里
     */
    @GetMapping("/calculate-calories/{userId}")
    public Result<Integer> calculateTargetCalories(@PathVariable Long userId) {
        return healthProfileService.calculateTargetCalories(userId);
    }
    
    /**
     * 计算营养比例
     */
    @GetMapping("/calculate-ratios/{userId}")
    public Result<Map<String, Double>> calculateNutritionRatios(@PathVariable Long userId) {
        return healthProfileService.calculateNutritionRatios(userId);
    }
    
    /**
     * 计算三餐营养分配
     */
    @GetMapping("/calculate-meals/{userId}")
    public Result<Map<String, Map<String, Double>>> calculateMealNutritionDistribution(@PathVariable Long userId) {
        return healthProfileService.calculateMealNutritionDistribution(userId);
    }
    
    /**
     * 生成营养策略
     */
    @GetMapping("/generate-strategy/{userId}")
    public Result<String> generateNutritionStrategy(@PathVariable Long userId) {
        return healthProfileService.generateNutritionStrategy(userId);
    }
    
    /**
     * 激活健康档案
     */
    @PutMapping("/activate/{id}")
    public Result<UserHealthProfile> activateHealthProfile(@PathVariable Long id) {
        return healthProfileService.activateHealthProfile(id);
    }
}
