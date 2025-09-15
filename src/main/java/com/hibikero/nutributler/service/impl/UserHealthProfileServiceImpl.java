package com.hibikero.nutributler.service.impl;

import com.hibikero.nutributler.common.Result;
import com.hibikero.nutributler.entity.UserHealthProfile;
import com.hibikero.nutributler.entity.UserSimple;
import com.hibikero.nutributler.repository.UserHealthProfileRepository;
import com.hibikero.nutributler.repository.UserSimpleRepository;
import com.hibikero.nutributler.service.NutritionCalculationService;
import com.hibikero.nutributler.service.UserHealthProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * 用户健康档案服务实现类
 */
@Service
public class UserHealthProfileServiceImpl implements UserHealthProfileService {
    
    @Autowired
    private UserHealthProfileRepository healthProfileRepository;
    
    @Autowired
    private UserSimpleRepository userRepository;
    
    @Autowired
    private NutritionCalculationService nutritionCalculationService;
    
    @Override
    public Result<UserHealthProfile> createHealthProfile(UserHealthProfile healthProfile) {
        try {
            // 检查用户是否存在
            Optional<UserSimple> userOpt = userRepository.findById(healthProfile.getUserId());
            if (!userOpt.isPresent()) {
                return Result.error("用户不存在");
            }
            
            // 检查是否已有激活的档案
            Optional<UserHealthProfile> existingProfile = healthProfileRepository.findActiveProfileByUserId(healthProfile.getUserId());
            if (existingProfile.isPresent()) {
                return Result.error("用户已有激活的健康档案，请先更新现有档案");
            }
            
            // 计算目标卡路里
            UserSimple user = userOpt.get();
            int targetCalories = nutritionCalculationService.calculateTargetCalories(user, healthProfile);
            healthProfile.setTargetCalories(targetCalories);
            
            // 计算营养比例
            Map<String, Double> nutritionRatios = nutritionCalculationService.calculateNutritionRatios(healthProfile);
            healthProfile.setTargetProteinRatio(nutritionRatios.get("protein") * 100);
            healthProfile.setTargetCarbRatio(nutritionRatios.get("carbohydrates") * 100);
            healthProfile.setTargetFatRatio(nutritionRatios.get("fat") * 100);
            
            // 生成营养策略
            String nutritionStrategy = nutritionCalculationService.generateNutritionStrategy(healthProfile);
            healthProfile.setNutritionStrategy(nutritionStrategy);
            
            // 设置默认状态
            if (healthProfile.getStatus() == null) {
                healthProfile.setStatus(1); // 激活状态
            }
            
            UserHealthProfile savedProfile = healthProfileRepository.save(healthProfile);
            return Result.success(savedProfile);
        } catch (Exception e) {
            return Result.error("创建健康档案失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<UserHealthProfile> updateHealthProfile(UserHealthProfile healthProfile) {
        try {
            // 检查档案是否存在
            Optional<UserHealthProfile> existingProfile = healthProfileRepository.findById(healthProfile.getId());
            if (!existingProfile.isPresent()) {
                return Result.error("健康档案不存在");
            }
            
            // 重新计算目标卡路里和营养比例
            Optional<UserSimple> userOpt = userRepository.findById(healthProfile.getUserId());
            if (userOpt.isPresent()) {
                UserSimple user = userOpt.get();
                int targetCalories = nutritionCalculationService.calculateTargetCalories(user, healthProfile);
                healthProfile.setTargetCalories(targetCalories);
                
                Map<String, Double> nutritionRatios = nutritionCalculationService.calculateNutritionRatios(healthProfile);
                healthProfile.setTargetProteinRatio(nutritionRatios.get("protein") * 100);
                healthProfile.setTargetCarbRatio(nutritionRatios.get("carbohydrates") * 100);
                healthProfile.setTargetFatRatio(nutritionRatios.get("fat") * 100);
                
                String nutritionStrategy = nutritionCalculationService.generateNutritionStrategy(healthProfile);
                healthProfile.setNutritionStrategy(nutritionStrategy);
            }
            
            UserHealthProfile updatedProfile = healthProfileRepository.save(healthProfile);
            return Result.success(updatedProfile);
        } catch (Exception e) {
            return Result.error("更新健康档案失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<UserHealthProfile> getHealthProfileByUserId(Long userId) {
        try {
            Optional<UserHealthProfile> profileOpt = healthProfileRepository.findActiveProfileByUserId(userId);
            if (profileOpt.isPresent()) {
                return Result.success(profileOpt.get());
            } else {
                return Result.error("用户健康档案不存在");
            }
        } catch (Exception e) {
            return Result.error("获取健康档案失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<UserHealthProfile> getHealthProfileById(Long id) {
        try {
            Optional<UserHealthProfile> profileOpt = healthProfileRepository.findById(id);
            if (profileOpt.isPresent()) {
                UserHealthProfile profile = profileOpt.get();
                if (profile.getDeleted() == 1) {
                    return Result.error("健康档案不存在");
                }
                return Result.success(profile);
            } else {
                return Result.error("健康档案不存在");
            }
        } catch (Exception e) {
            return Result.error("获取健康档案失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<Void> deleteHealthProfile(Long id) {
        try {
            Optional<UserHealthProfile> profileOpt = healthProfileRepository.findById(id);
            if (!profileOpt.isPresent()) {
                return Result.error("健康档案不存在");
            }
            
            UserHealthProfile profile = profileOpt.get();
            profile.setDeleted(1);
            healthProfileRepository.save(profile);
            
            return Result.success(null);
        } catch (Exception e) {
            return Result.error("删除健康档案失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<Integer> calculateTargetCalories(Long userId) {
        try {
            Optional<UserSimple> userOpt = userRepository.findById(userId);
            if (!userOpt.isPresent()) {
                return Result.error("用户不存在");
            }
            
            Optional<UserHealthProfile> profileOpt = healthProfileRepository.findActiveProfileByUserId(userId);
            if (!profileOpt.isPresent()) {
                return Result.error("用户健康档案不存在");
            }
            
            UserSimple user = userOpt.get();
            UserHealthProfile profile = profileOpt.get();
            int targetCalories = nutritionCalculationService.calculateTargetCalories(user, profile);
            
            return Result.success(targetCalories);
        } catch (Exception e) {
            return Result.error("计算目标卡路里失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<Map<String, Double>> calculateNutritionRatios(Long userId) {
        try {
            Optional<UserHealthProfile> profileOpt = healthProfileRepository.findActiveProfileByUserId(userId);
            if (!profileOpt.isPresent()) {
                return Result.error("用户健康档案不存在");
            }
            
            UserHealthProfile profile = profileOpt.get();
            Map<String, Double> ratios = nutritionCalculationService.calculateNutritionRatios(profile);
            
            return Result.success(ratios);
        } catch (Exception e) {
            return Result.error("计算营养比例失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<Map<String, Map<String, Double>>> calculateMealNutritionDistribution(Long userId) {
        try {
            Optional<UserHealthProfile> profileOpt = healthProfileRepository.findActiveProfileByUserId(userId);
            if (!profileOpt.isPresent()) {
                return Result.error("用户健康档案不存在");
            }
            
            UserHealthProfile profile = profileOpt.get();
            Map<String, Double> nutritionRatios = nutritionCalculationService.calculateNutritionRatios(profile);
            Map<String, Map<String, Double>> mealDistribution = nutritionCalculationService
                .calculateMealNutritionDistribution(profile.getTargetCalories(), nutritionRatios);
            
            return Result.success(mealDistribution);
        } catch (Exception e) {
            return Result.error("计算三餐营养分配失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<String> generateNutritionStrategy(Long userId) {
        try {
            Optional<UserHealthProfile> profileOpt = healthProfileRepository.findActiveProfileByUserId(userId);
            if (!profileOpt.isPresent()) {
                return Result.error("用户健康档案不存在");
            }
            
            UserHealthProfile profile = profileOpt.get();
            String strategy = nutritionCalculationService.generateNutritionStrategy(profile);
            
            return Result.success(strategy);
        } catch (Exception e) {
            return Result.error("生成营养策略失败：" + e.getMessage());
        }
    }
    
    @Override
    public Result<UserHealthProfile> activateHealthProfile(Long id) {
        try {
            Optional<UserHealthProfile> profileOpt = healthProfileRepository.findById(id);
            if (!profileOpt.isPresent()) {
                return Result.error("健康档案不存在");
            }
            
            UserHealthProfile profile = profileOpt.get();
            profile.setStatus(1); // 激活状态
            UserHealthProfile updatedProfile = healthProfileRepository.save(profile);
            
            return Result.success(updatedProfile);
        } catch (Exception e) {
            return Result.error("激活健康档案失败：" + e.getMessage());
        }
    }
}
