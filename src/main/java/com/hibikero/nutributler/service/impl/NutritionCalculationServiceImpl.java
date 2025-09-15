package com.hibikero.nutributler.service.impl;

import com.hibikero.nutributler.entity.UserHealthProfile;
import com.hibikero.nutributler.entity.UserSimple;
import com.hibikero.nutributler.service.NutritionCalculationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 营养计算服务实现类
 */
@Service
public class NutritionCalculationServiceImpl implements NutritionCalculationService {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public double calculateBMR(UserSimple user) {
        if (user.getGender() == null || user.getWeight() == null || 
            user.getHeight() == null || user.getAge() == null) {
            return 1500; // 默认值
        }
        
        // 使用Mifflin-St Jeor公式计算BMR
        double bmr;
        if (user.getGender() == 1) { // 男性
            bmr = 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * user.getAge() + 5;
        } else { // 女性
            bmr = 10 * user.getWeight() + 6.25 * user.getHeight() - 5 * user.getAge() - 161;
        }
        
        return Math.max(bmr, 800); // 最低800卡路里
    }
    
    @Override
    public double calculateTDEE(UserSimple user) {
        double bmr = calculateBMR(user);
        
        // 活动系数
        double activityMultiplier = getActivityMultiplier(user.getActivityLevel());
        
        return bmr * activityMultiplier;
    }
    
    @Override
    public int calculateTargetCalories(UserSimple user, UserHealthProfile healthProfile) {
        double tdee = calculateTDEE(user);
        
        // 根据健康目标调整卡路里
        double targetCalories = adjustCaloriesForGoal(tdee, healthProfile.getHealthGoal());
        
        return (int) Math.round(targetCalories);
    }
    
    @Override
    public Map<String, Double> calculateNutritionRatios(UserHealthProfile healthProfile) {
        Map<String, Double> ratios = new HashMap<>();
        
        // 根据健康目标设置营养比例
        switch (healthProfile.getHealthGoal()) {
            case 1: // 身材管理
                ratios.put("protein", 0.25);
                ratios.put("carbohydrates", 0.45);
                ratios.put("fat", 0.30);
                break;
            case 2: // 改善精力
                ratios.put("protein", 0.20);
                ratios.put("carbohydrates", 0.50);
                ratios.put("fat", 0.30);
                break;
            case 3: // 提升睡眠质量
                ratios.put("protein", 0.20);
                ratios.put("carbohydrates", 0.50);
                ratios.put("fat", 0.30);
                break;
            case 4: // 增强免疫力
                ratios.put("protein", 0.25);
                ratios.put("carbohydrates", 0.45);
                ratios.put("fat", 0.30);
                break;
            case 5: // 控制血糖
                ratios.put("protein", 0.30);
                ratios.put("carbohydrates", 0.40);
                ratios.put("fat", 0.30);
                break;
            default:
                ratios.put("protein", 0.25);
                ratios.put("carbohydrates", 0.45);
                ratios.put("fat", 0.30);
        }
        
        return ratios;
    }
    
    @Override
    public Map<String, Map<String, Double>> calculateMealNutritionDistribution(int targetCalories, Map<String, Double> nutritionRatios) {
        Map<String, Map<String, Double>> mealDistribution = new HashMap<>();
        
        // 三餐卡路里分配：早餐30%，午餐40%，晚餐30%
        int breakfastCalories = (int) (targetCalories * 0.30);
        int lunchCalories = (int) (targetCalories * 0.40);
        int dinnerCalories = (int) (targetCalories * 0.30);
        
        // 计算每餐的营养分配
        mealDistribution.put("breakfast", calculateMealNutrition(breakfastCalories, nutritionRatios));
        mealDistribution.put("lunch", calculateMealNutrition(lunchCalories, nutritionRatios));
        mealDistribution.put("dinner", calculateMealNutrition(dinnerCalories, nutritionRatios));
        
        return mealDistribution;
    }
    
    @Override
    public String generateNutritionStrategy(UserHealthProfile healthProfile) {
        Map<String, Object> strategy = new HashMap<>();
        
        // 根据健康目标设置营养策略
        switch (healthProfile.getHealthGoal()) {
            case 1: // 身材管理
                strategy.put("focusNutrients", new String[]{"蛋白质", "纤维", "维生素B"});
                strategy.put("greenFoods", new String[]{"鸡胸肉", "鱼类", "蔬菜", "全谷物"});
                strategy.put("yellowFoods", new String[]{"坚果", "水果", "乳制品"});
                strategy.put("redFoods", new String[]{"油炸食品", "高糖饮料", "加工食品"});
                strategy.put("mealFrequency", "一日三餐，适量加餐");
                strategy.put("hydration", "每日2-3升水");
                break;
            case 2: // 改善精力
                strategy.put("focusNutrients", new String[]{"铁", "维生素B12", "叶酸", "维生素C"});
                strategy.put("greenFoods", new String[]{"瘦肉", "豆类", "绿叶蔬菜", "坚果"});
                strategy.put("yellowFoods", new String[]{"全谷物", "水果", "乳制品"});
                strategy.put("redFoods", new String[]{"精制糖", "咖啡因过量", "酒精"});
                strategy.put("mealFrequency", "少食多餐，保持血糖稳定");
                strategy.put("hydration", "每日2-3升水，适量咖啡因");
                break;
            case 3: // 提升睡眠质量
                strategy.put("focusNutrients", new String[]{"镁", "色氨酸", "维生素B6", "钙"});
                strategy.put("greenFoods", new String[]{"香蕉", "燕麦", "杏仁", "牛奶"});
                strategy.put("yellowFoods", new String[]{"全谷物", "坚果", "鱼类"});
                strategy.put("redFoods", new String[]{"咖啡因", "酒精", "高糖食物"});
                strategy.put("mealFrequency", "晚餐清淡，睡前2小时不进食");
                strategy.put("hydration", "下午后减少饮水");
                break;
            case 4: // 增强免疫力
                strategy.put("focusNutrients", new String[]{"维生素C", "维生素D", "锌", "硒"});
                strategy.put("greenFoods", new String[]{"柑橘类", "浆果", "绿叶蔬菜", "坚果"});
                strategy.put("yellowFoods", new String[]{"鱼类", "全谷物", "乳制品"});
                strategy.put("redFoods", new String[]{"加工食品", "高糖食物", "酒精"});
                strategy.put("mealFrequency", "均衡三餐，多样化饮食");
                strategy.put("hydration", "每日2-3升水，适量绿茶");
                break;
            case 5: // 控制血糖
                strategy.put("focusNutrients", new String[]{"纤维", "蛋白质", "健康脂肪", "铬"});
                strategy.put("greenFoods", new String[]{"绿叶蔬菜", "豆类", "全谷物", "坚果"});
                strategy.put("yellowFoods", new String[]{"鱼类", "瘦肉", "低糖水果"});
                strategy.put("redFoods", new String[]{"精制糖", "白米白面", "高糖饮料"});
                strategy.put("mealFrequency", "定时定量，少食多餐");
                strategy.put("hydration", "每日2-3升水，避免含糖饮料");
                break;
        }
        
        try {
            return objectMapper.writeValueAsString(strategy);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
    
    /**
     * 获取活动系数
     */
    private double getActivityMultiplier(Integer activityLevel) {
        if (activityLevel == null) {
            return 1.375; // 默认轻度活动
        }
        
        switch (activityLevel) {
            case 1: return 1.2;   // 久坐
            case 2: return 1.375; // 轻度活动
            case 3: return 1.55;  // 中度活动
            case 4: return 1.725; // 高度活动
            case 5: return 1.9;   // 极高活动
            default: return 1.375;
        }
    }
    
    /**
     * 根据健康目标调整卡路里
     */
    private double adjustCaloriesForGoal(double tdee, Integer healthGoal) {
        if (healthGoal == null) {
            return tdee;
        }
        
        switch (healthGoal) {
            case 1: // 身材管理 - 减脂
                return tdee * 0.8; // 减少20%
            case 2: // 改善精力 - 维持
                return tdee;
            case 3: // 提升睡眠质量 - 维持
                return tdee;
            case 4: // 增强免疫力 - 略增
                return tdee * 1.05; // 增加5%
            case 5: // 控制血糖 - 维持
                return tdee;
            default:
                return tdee;
        }
    }
    
    /**
     * 计算单餐营养分配
     */
    private Map<String, Double> calculateMealNutrition(int calories, Map<String, Double> ratios) {
        Map<String, Double> mealNutrition = new HashMap<>();
        
        // 蛋白质：4卡路里/克
        mealNutrition.put("protein", (calories * ratios.get("protein")) / 4);
        
        // 碳水化合物：4卡路里/克
        mealNutrition.put("carbohydrates", (calories * ratios.get("carbohydrates")) / 4);
        
        // 脂肪：9卡路里/克
        mealNutrition.put("fat", (calories * ratios.get("fat")) / 9);
        
        return mealNutrition;
    }
}