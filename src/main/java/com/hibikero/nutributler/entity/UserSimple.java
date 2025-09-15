package com.hibikero.nutributler.entity;

import com.hibikero.nutributler.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户实体类（简化版）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user")
public class UserSimple extends BaseEntity {

    /**
     * 用户名
     */
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    /**
     * 密码
     */
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    /**
     * 邮箱
     */
    @Column(name = "email", length = 100)
    private String email;

    /**
     * 手机号
     */
    @Column(name = "phone", length = 20)
    private String phone;

    /**
     * 昵称
     */
    @Column(name = "nickname", length = 50)
    private String nickname;

    /**
     * 头像URL
     */
    @Column(name = "avatar", length = 255)
    private String avatar;

    /**
     * 性别（0：未知，1：男，2：女）
     */
    @Column(name = "gender")
    private Integer gender;

    /**
     * 年龄
     */
    @Column(name = "age")
    private Integer age;

    /**
     * 身高（cm）
     */
    @Column(name = "height")
    private Double height;

    /**
     * 体重（kg）
     */
    @Column(name = "weight")
    private Double weight;

    /**
     * 活动水平（1：久坐，2：轻度活动，3：中度活动，4：高度活动，5：极高活动）
     */
    @Column(name = "activity_level")
    private Integer activityLevel;

    /**
     * 用户目标（1：增肌，2：减脂，3：维持，4：改善精力，5：提升睡眠质量）
     */
    @Column(name = "health_goal")
    private Integer healthGoal;

    /**
     * 目标实现时间（月）
     */
    @Column(name = "goal_duration")
    private Integer goalDuration;

    /**
     * 忌口食物（JSON格式存储）
     */
    @Column(name = "avoid_foods", columnDefinition = "TEXT")
    private String avoidFoods;

    /**
     * 过敏源（JSON格式存储）
     */
    @Column(name = "allergies", columnDefinition = "TEXT")
    private String allergies;

    /**
     * 用户状态（0：禁用，1：正常）
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;
}