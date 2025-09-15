-- 创建数据库
CREATE DATABASE IF NOT EXISTS nutri_butler CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE nutri_butler;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` varchar(50) NOT NULL COMMENT '用户名',
    `password` varchar(255) NOT NULL COMMENT '密码',
    `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
    `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
    `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
    `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
    `gender` tinyint DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    `age` int DEFAULT NULL COMMENT '年龄',
    `height` decimal(5,2) DEFAULT NULL COMMENT '身高(cm)',
    `weight` decimal(5,2) DEFAULT NULL COMMENT '体重(kg)',
    `activity_level` tinyint DEFAULT NULL COMMENT '活动水平：1-久坐，2-轻度活动，3-中度活动，4-高度活动，5-极高活动',
    `status` tinyint DEFAULT 1 COMMENT '用户状态：0-禁用，1-正常',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_email` (`email`),
    KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 用户健康档案表
CREATE TABLE IF NOT EXISTS `user_health_profile` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '档案ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `health_goal` tinyint NOT NULL COMMENT '健康目标：1-身材管理，2-改善精力，3-提升睡眠质量，4-增强免疫力，5-控制血糖',
    `goal_description` varchar(500) DEFAULT NULL COMMENT '目标描述',
    `target_calories` int DEFAULT NULL COMMENT '目标卡路里（每日）',
    `target_protein_ratio` decimal(5,2) DEFAULT NULL COMMENT '目标蛋白质比例（%）',
    `target_carb_ratio` decimal(5,2) DEFAULT NULL COMMENT '目标碳水化合物比例（%）',
    `target_fat_ratio` decimal(5,2) DEFAULT NULL COMMENT '目标脂肪比例（%）',
    `allergies` text COMMENT '过敏源（JSON格式存储）',
    `dietary_preferences` text COMMENT '饮食偏好（JSON格式存储）',
    `avoid_foods` text COMMENT '忌口食物（JSON格式存储）',
    `special_needs` text COMMENT '特殊需求（JSON格式存储）',
    `nutrition_strategy` text COMMENT '营养策略档案（JSON格式存储）',
    `status` tinyint DEFAULT 1 COMMENT '档案状态：0-草稿，1-已激活',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_health_goal` (`health_goal`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户健康档案表';

-- 食材分类表
CREATE TABLE IF NOT EXISTS `ingredient_category` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `name` varchar(100) NOT NULL COMMENT '分类名称',
    `description` varchar(500) DEFAULT NULL COMMENT '分类描述',
    `parent_id` bigint DEFAULT NULL COMMENT '父分类ID',
    `icon` varchar(255) DEFAULT NULL COMMENT '分类图标',
    `sort_order` int DEFAULT 0 COMMENT '排序',
    `status` tinyint DEFAULT 1 COMMENT '分类状态：0-禁用，1-正常',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='食材分类表';

-- 食材表
CREATE TABLE IF NOT EXISTS `ingredient` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '食材ID',
    `name` varchar(100) NOT NULL COMMENT '食材名称',
    `category_id` bigint DEFAULT NULL COMMENT '食材分类ID',
    `description` varchar(500) DEFAULT NULL COMMENT '食材描述',
    `calories` decimal(8,2) DEFAULT NULL COMMENT '每100g卡路里',
    `protein` decimal(8,2) DEFAULT NULL COMMENT '每100g蛋白质(g)',
    `carbohydrates` decimal(8,2) DEFAULT NULL COMMENT '每100g碳水化合物(g)',
    `fat` decimal(8,2) DEFAULT NULL COMMENT '每100g脂肪(g)',
    `fiber` decimal(8,2) DEFAULT NULL COMMENT '每100g纤维(g)',
    `sugar` decimal(8,2) DEFAULT NULL COMMENT '每100g糖分(g)',
    `sodium` decimal(8,2) DEFAULT NULL COMMENT '每100g钠(mg)',
    `vitamin_c` decimal(8,2) DEFAULT NULL COMMENT '每100g维生素C(mg)',
    `iron` decimal(8,2) DEFAULT NULL COMMENT '每100g铁(mg)',
    `calcium` decimal(8,2) DEFAULT NULL COMMENT '每100g钙(mg)',
    `vitamin_d` decimal(8,2) DEFAULT NULL COMMENT '每100g维生素D(IU)',
    `image_url` varchar(255) DEFAULT NULL COMMENT '食材图片URL',
    `status` tinyint DEFAULT 1 COMMENT '食材状态：0-禁用，1-正常',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_name` (`name`),
    KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='食材表';

-- 用户食品柜表
CREATE TABLE IF NOT EXISTS `user_pantry` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '食品柜记录ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `ingredient_id` bigint NOT NULL COMMENT '食材ID',
    `ingredient_name` varchar(100) NOT NULL COMMENT '食材名称（冗余字段）',
    `quantity` decimal(10,2) NOT NULL COMMENT '数量',
    `unit` varchar(20) NOT NULL COMMENT '单位',
    `purchase_date` date DEFAULT NULL COMMENT '购买日期',
    `expiry_date` date DEFAULT NULL COMMENT '保质期',
    `storage_location` varchar(100) DEFAULT NULL COMMENT '存储位置',
    `notes` varchar(500) DEFAULT NULL COMMENT '备注',
    `status` tinyint DEFAULT 1 COMMENT '状态：0-已用完，1-正常，2-即将过期，3-已过期',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_ingredient_id` (`ingredient_id`),
    KEY `idx_expiry_date` (`expiry_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户食品柜表';

-- 食谱分类表
CREATE TABLE IF NOT EXISTS `recipe_category` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `name` varchar(100) NOT NULL COMMENT '分类名称',
    `description` varchar(500) DEFAULT NULL COMMENT '分类描述',
    `parent_id` bigint DEFAULT NULL COMMENT '父分类ID',
    `icon` varchar(255) DEFAULT NULL COMMENT '分类图标',
    `sort_order` int DEFAULT 0 COMMENT '排序',
    `status` tinyint DEFAULT 1 COMMENT '分类状态：0-禁用，1-正常',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='食谱分类表';

-- 食谱表
CREATE TABLE IF NOT EXISTS `recipe` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '食谱ID',
    `name` varchar(200) NOT NULL COMMENT '食谱名称',
    `description` text COMMENT '食谱描述',
    `creator_id` bigint NOT NULL COMMENT '创建者用户ID',
    `category_id` bigint DEFAULT NULL COMMENT '食谱分类ID',
    `difficulty` tinyint DEFAULT 1 COMMENT '烹饪难度：1-简单，2-中等，3-困难',
    `prep_time` int DEFAULT NULL COMMENT '准备时间（分钟）',
    `cook_time` int DEFAULT NULL COMMENT '烹饪时间（分钟）',
    `total_time` int DEFAULT NULL COMMENT '总时间（分钟）',
    `servings` int DEFAULT 1 COMMENT '份数',
    `calories_per_serving` decimal(8,2) DEFAULT NULL COMMENT '每份卡路里',
    `protein_per_serving` decimal(8,2) DEFAULT NULL COMMENT '每份蛋白质(g)',
    `carbs_per_serving` decimal(8,2) DEFAULT NULL COMMENT '每份碳水化合物(g)',
    `fat_per_serving` decimal(8,2) DEFAULT NULL COMMENT '每份脂肪(g)',
    `image_url` varchar(255) DEFAULT NULL COMMENT '食谱图片URL',
    `cooking_steps` text COMMENT '烹饪步骤（JSON格式存储）',
    `cooking_tips` text COMMENT '烹饪技巧（JSON格式存储）',
    `health_tags` text COMMENT '健康标签（JSON格式存储）',
    `status` tinyint DEFAULT 0 COMMENT '食谱状态：0-草稿，1-已发布，2-已下架',
    `favorite_count` int DEFAULT 0 COMMENT '收藏数',
    `cook_count` int DEFAULT 0 COMMENT '制作次数',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_creator_id` (`creator_id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_difficulty` (`difficulty`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='食谱表';

-- 食谱食材关联表
CREATE TABLE IF NOT EXISTS `recipe_ingredient` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关联ID',
    `recipe_id` bigint NOT NULL COMMENT '食谱ID',
    `ingredient_id` bigint NOT NULL COMMENT '食材ID',
    `ingredient_name` varchar(100) NOT NULL COMMENT '食材名称（冗余字段）',
    `quantity` decimal(10,2) NOT NULL COMMENT '用量',
    `unit` varchar(20) NOT NULL COMMENT '单位',
    `is_required` tinyint DEFAULT 1 COMMENT '是否为必需食材：0-可选，1-必需',
    `alternatives` text COMMENT '替代食材（JSON格式存储）',
    `notes` varchar(500) DEFAULT NULL COMMENT '备注',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_recipe_id` (`recipe_id`),
    KEY `idx_ingredient_id` (`ingredient_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='食谱食材关联表';

-- 膳食计划表
CREATE TABLE IF NOT EXISTS `meal_plan` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '计划ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `plan_date` date NOT NULL COMMENT '计划日期',
    `meal_type` tinyint NOT NULL COMMENT '餐次类型：1-早餐，2-午餐，3-晚餐，4-加餐',
    `recipe_id` bigint DEFAULT NULL COMMENT '食谱ID',
    `status` tinyint DEFAULT 0 COMMENT '计划状态：0-未完成，1-已完成，2-已跳过',
    `actual_cook_time` datetime DEFAULT NULL COMMENT '实际制作时间',
    `rating` tinyint DEFAULT NULL COMMENT '用户评分（1-5分）',
    `feedback` text COMMENT '用户反馈',
    `is_auto_generated` tinyint DEFAULT 0 COMMENT '是否自动生成：0-手动添加，1-自动生成',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_plan_date` (`plan_date`),
    KEY `idx_meal_type` (`meal_type`),
    KEY `idx_recipe_id` (`recipe_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='膳食计划表';

-- 用户反馈表
CREATE TABLE IF NOT EXISTS `user_feedback` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '反馈ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `feedback_type` tinyint NOT NULL COMMENT '反馈类型：1-目标进度，2-精力水平，3-睡眠质量，4-整体满意度',
    `period_start` date NOT NULL COMMENT '反馈周期开始日期',
    `period_end` date NOT NULL COMMENT '反馈周期结束日期',
    `score` tinyint NOT NULL COMMENT '评分（1-5分）',
    `content` text COMMENT '反馈内容',
    `related_data` text COMMENT '相关数据（JSON格式存储）',
    `process_status` tinyint DEFAULT 0 COMMENT '处理状态：0-未处理，1-已处理',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_feedback_type` (`feedback_type`),
    KEY `idx_period_start` (`period_start`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户反馈表';

-- 插入初始数据
INSERT INTO `ingredient_category` (`name`, `description`, `sort_order`) VALUES
('蔬菜类', '各种蔬菜', 1),
('肉类', '各种肉类', 2),
('蛋奶类', '鸡蛋、牛奶等', 3),
('谷物类', '大米、小麦等', 4),
('水果类', '各种水果', 5);

INSERT INTO `recipe_category` (`name`, `description`, `sort_order`) VALUES
('早餐', '早餐食谱', 1),
('午餐', '午餐食谱', 2),
('晚餐', '晚餐食谱', 3),
('加餐', '加餐食谱', 4),
('汤品', '各种汤品', 5);

INSERT INTO `ingredient` (`name`, `category_id`, `calories`, `protein`, `carbohydrates`, `fat`, `fiber`, `sodium`, `vitamin_c`, `iron`, `calcium`) VALUES
('鸡胸肉', 2, 165.00, 31.00, 0.00, 3.60, 0.00, 74.00, 0.00, 0.70, 15.00),
('西兰花', 1, 34.00, 2.82, 6.64, 0.37, 2.60, 33.00, 89.20, 0.73, 47.00),
('鸡蛋', 3, 155.00, 13.00, 1.10, 11.00, 0.00, 124.00, 0.00, 1.20, 56.00),
('大米', 4, 130.00, 2.70, 28.00, 0.30, 0.40, 5.00, 0.00, 0.80, 28.00),
('苹果', 5, 52.00, 0.26, 13.81, 0.17, 2.40, 1.00, 4.60, 0.12, 6.00);
