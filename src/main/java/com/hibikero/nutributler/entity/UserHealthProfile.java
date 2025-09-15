package com.hibikero.nutributler.entity;

import com.hibikero.nutributler.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户健康档案实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_health_profile")
public class UserHealthProfile extends BaseEntity {
    
    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    /**
     * 健康目标：1-身材管理，2-改善精力，3-提升睡眠质量，4-增强免疫力，5-控制血糖
     */
    @Column(name = "health_goal", nullable = false)
    private Integer healthGoal;
    
    /**
     * 目标描述
     */
    @Column(name = "goal_description", length = 500)
    private String goalDescription;
    
    /**
     * 目标卡路里（每日）
     */
    @Column(name = "target_calories")
    private Integer targetCalories;
    
    /**
     * 目标蛋白质比例（%）
     */
    @Column(name = "target_protein_ratio")
    private Double targetProteinRatio;
    
    /**
     * 目标碳水化合物比例（%）
     */
    @Column(name = "target_carb_ratio")
    private Double targetCarbRatio;
    
    /**
     * 目标脂肪比例（%）
     */
    @Column(name = "target_fat_ratio")
    private Double targetFatRatio;
    
    /**
     * 过敏源（JSON格式存储）
     */
    @Column(name = "allergies", columnDefinition = "TEXT")
    private String allergies;
    
    /**
     * 饮食偏好（JSON格式存储）
     */
    @Column(name = "dietary_preferences", columnDefinition = "TEXT")
    private String dietaryPreferences;
    
    /**
     * 忌口食物（JSON格式存储）
     */
    @Column(name = "avoid_foods", columnDefinition = "TEXT")
    private String avoidFoods;
    
    /**
     * 特殊需求（JSON格式存储）
     */
    @Column(name = "special_needs", columnDefinition = "TEXT")
    private String specialNeeds;
    
    /**
     * 营养策略档案（JSON格式存储）
     */
    @Column(name = "nutrition_strategy", columnDefinition = "TEXT")
    private String nutritionStrategy;
    
    /**
     * 档案状态：0-草稿，1-已激活
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;
}
