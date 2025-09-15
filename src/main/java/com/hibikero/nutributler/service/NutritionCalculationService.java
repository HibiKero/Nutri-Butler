package com.hibikero.nutributler.service;

import com.hibikero.nutributler.entity.UserHealthProfile;
import com.hibikero.nutributler.entity.UserSimple;

import java.util.Map;

/**
 * 营养计算服务接口
 */
public interface NutritionCalculationService {
    
    /**
     * 计算基础代谢率（BMR）
     * @param user 用户信息
     * @return BMR值
     */
    double calculateBMR(UserSimple user);
    
    /**
     * 计算总消耗卡路里（TDEE）
     * @param user 用户信息
     * @return TDEE值
     */
    double calculateTDEE(UserSimple user);
    
    /**
     * 计算目标卡路里
     * @param user 用户信息
     * @param healthProfile 健康档案
     * @return 目标卡路里
     */
    int calculateTargetCalories(UserSimple user, UserHealthProfile healthProfile);
    
    /**
     * 计算营养比例
     * @param healthProfile 健康档案
     * @return 营养比例Map
     */
    Map<String, Double> calculateNutritionRatios(UserHealthProfile healthProfile);
    
    /**
     * 计算三餐营养分配
     * @param targetCalories 目标卡路里
     * @param nutritionRatios 营养比例
     * @return 三餐营养分配
     */
    Map<String, Map<String, Double>> calculateMealNutritionDistribution(int targetCalories, Map<String, Double> nutritionRatios);
    
    /**
     * 生成营养策略
     * @param healthProfile 健康档案
     * @return 营养策略JSON字符串
     */
    String generateNutritionStrategy(UserHealthProfile healthProfile);
}
