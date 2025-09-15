package com.hibikero.nutributler.service;

import com.hibikero.nutributler.common.Result;
import com.hibikero.nutributler.entity.UserHealthProfile;

import java.util.Map;

/**
 * 用户健康档案服务接口
 */
public interface UserHealthProfileService {
    
    /**
     * 创建健康档案
     */
    Result<UserHealthProfile> createHealthProfile(UserHealthProfile healthProfile);
    
    /**
     * 更新健康档案
     */
    Result<UserHealthProfile> updateHealthProfile(UserHealthProfile healthProfile);
    
    /**
     * 根据用户ID获取健康档案
     */
    Result<UserHealthProfile> getHealthProfileByUserId(Long userId);
    
    /**
     * 根据ID获取健康档案
     */
    Result<UserHealthProfile> getHealthProfileById(Long id);
    
    /**
     * 删除健康档案
     */
    Result<Void> deleteHealthProfile(Long id);
    
    /**
     * 计算目标卡路里
     */
    Result<Integer> calculateTargetCalories(Long userId);
    
    /**
     * 计算营养比例
     */
    Result<Map<String, Double>> calculateNutritionRatios(Long userId);
    
    /**
     * 计算三餐营养分配
     */
    Result<Map<String, Map<String, Double>>> calculateMealNutritionDistribution(Long userId);
    
    /**
     * 生成营养策略
     */
    Result<String> generateNutritionStrategy(Long userId);
    
    /**
     * 激活健康档案
     */
    Result<UserHealthProfile> activateHealthProfile(Long id);
}
