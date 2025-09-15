package com.hibikero.nutributler.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * 数据库初始化上下文初始化器
 * 在应用上下文初始化之前执行数据库创建
 */
public class DatabaseInitializerContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        
        String dataSourceUrl = environment.getProperty("spring.datasource.url");
        String username = environment.getProperty("spring.datasource.username");
        String password = environment.getProperty("spring.datasource.password");
        
        System.out.println("=== 数据库初始化器启动 ===");
        System.out.println("数据源URL: " + dataSourceUrl);
        System.out.println("用户名: " + username);
        
        if (dataSourceUrl != null && username != null && password != null) {
            try {
                initializeDatabase(dataSourceUrl, username, password);
            } catch (Exception e) {
                System.err.println("数据库初始化失败: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("数据库配置信息不完整");
        }
    }

    /**
     * 初始化数据库
     */
    private void initializeDatabase(String dataSourceUrl, String username, String password) throws Exception {
        // 从URL中提取数据库名
        String databaseName = extractDatabaseName(dataSourceUrl);
        
        // 构建不指定数据库的URL - 正确的方式
        // 从 jdbc:mysql://localhost:3306/nutri_butler?params 构建为 jdbc:mysql://localhost:3306/mysql?params
        String serverUrl = dataSourceUrl.replace("/" + databaseName + "?", "/mysql?");
        if (!serverUrl.contains("?")) {
            serverUrl = dataSourceUrl.replace("/" + databaseName, "/mysql");
        }
        
        System.out.println("=== 数据库初始化开始 ===");
        System.out.println("正在检查数据库: " + databaseName);
        System.out.println("服务器URL: " + serverUrl);
        
        // 连接到MySQL服务器（不指定数据库）
        Connection connection = null;
        try {
            System.out.println("正在连接到MySQL服务器...");
            connection = DriverManager.getConnection(serverUrl, username, password);
            System.out.println("✅ 成功连接到MySQL服务器");
            
            // 检查数据库是否存在
            if (!databaseExists(connection, databaseName)) {
                System.out.println("数据库不存在，正在创建: " + databaseName);
                createDatabase(connection, databaseName);
                System.out.println("✅ 数据库创建成功: " + databaseName);
            } else {
                System.out.println("✅ 数据库已存在: " + databaseName);
            }
        } catch (Exception e) {
            System.err.println("连接MySQL服务器失败: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        
        // 连接到目标数据库
        Connection targetConnection = null;
        try {
            System.out.println("正在连接到目标数据库: " + databaseName);
            targetConnection = DriverManager.getConnection(dataSourceUrl, username, password);
            System.out.println("✅ 成功连接到目标数据库");
            
            // 检查表是否存在
            if (!tablesExist(targetConnection)) {
                System.out.println("正在创建表结构...");
                createTables(targetConnection);
                System.out.println("✅ 表结构创建成功");
            } else {
                System.out.println("✅ 表结构已存在");
            }
        } catch (Exception e) {
            System.err.println("连接目标数据库失败: " + e.getMessage());
            throw e;
        } finally {
            if (targetConnection != null) {
                targetConnection.close();
            }
        }
        
        System.out.println("=== 数据库初始化完成 ===");
    }

    /**
     * 从数据源URL中提取数据库名
     */
    private String extractDatabaseName(String url) {
        // 处理类似 jdbc:mysql://localhost:3306/nutri_butler?useUnicode=true&characterEncoding=utf8...
        // 找到端口号后的斜杠位置（数据库名前的斜杠）
        // 格式: jdbc:mysql://host:port/database?params
        int protocolEnd = url.indexOf("://");
        if (protocolEnd == -1) {
            throw new IllegalArgumentException("无效的数据库URL: " + url);
        }
        
        // 从协议后开始查找
        String afterProtocol = url.substring(protocolEnd + 3);
        
        // 查找第一个斜杠（数据库名前的斜杠）
        int firstSlash = afterProtocol.indexOf("/");
        if (firstSlash == -1) {
            throw new IllegalArgumentException("无法找到数据库名: " + url);
        }
        
        // 从斜杠后开始查找数据库名
        String afterSlash = afterProtocol.substring(firstSlash + 1);
        
        // 查找问号的位置（参数开始）
        int questionMark = afterSlash.indexOf("?");
        
        String databaseName;
        if (questionMark > 0) {
            databaseName = afterSlash.substring(0, questionMark);
        } else {
            databaseName = afterSlash;
        }
        
        // 验证数据库名不为空
        if (databaseName.isEmpty()) {
            throw new IllegalArgumentException("无法从URL中提取数据库名: " + url);
        }
        
        System.out.println("从URL提取的数据库名: " + databaseName);
        return databaseName;
    }

    /**
     * 检查数据库是否存在
     */
    private boolean databaseExists(Connection connection, String databaseName) throws Exception {
        String sql = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, databaseName);
            try (var resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    /**
     * 创建数据库
     */
    private void createDatabase(Connection connection, String databaseName) throws Exception {
        String sql = "CREATE DATABASE IF NOT EXISTS " + databaseName + 
                    " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
        
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    /**
     * 检查表是否存在
     */
    private boolean tablesExist(Connection connection) throws Exception {
        String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = DATABASE()";
        try (var statement = connection.prepareStatement(sql)) {
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * 创建表结构
     */
    private void createTables(Connection connection) throws Exception {
        // 创建用户表
        createUserTable(connection);
        
        // 创建用户健康档案表
        createUserHealthProfileTable(connection);
        
        // 创建食材分类表
        createIngredientCategoryTable(connection);
        
        // 创建食材表
        createIngredientTable(connection);
        
        // 创建用户食品柜表
        createUserPantryTable(connection);
        
        // 创建食谱分类表
        createRecipeCategoryTable(connection);
        
        // 创建食谱表
        createRecipeTable(connection);
        
        // 创建食谱食材关联表
        createRecipeIngredientTable(connection);
        
        // 创建膳食计划表
        createMealPlanTable(connection);
        
        // 创建用户反馈表
        createUserFeedbackTable(connection);
        
        // 插入初始数据
        insertInitialData(connection);
    }

    private void createUserTable(Connection connection) throws Exception {
        String sql = """
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
                `height` double DEFAULT NULL COMMENT '身高(cm)',
                `weight` double DEFAULT NULL COMMENT '体重(kg)',
                `activity_level` tinyint DEFAULT NULL COMMENT '活动水平：1-久坐，2-轻度活动，3-中度活动，4-高度活动，5-极高活动',
                `status` tinyint DEFAULT 1 COMMENT '用户状态：0-禁用，1-正常',
                `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                `deleted` tinyint DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
                PRIMARY KEY (`id`),
                UNIQUE KEY `uk_username` (`username`),
                KEY `idx_email` (`email`),
                KEY `idx_phone` (`phone`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表'
            """;
        
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    private void createUserHealthProfileTable(Connection connection) throws Exception {
        String sql = """
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
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户健康档案表'
            """;
        
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    private void createIngredientCategoryTable(Connection connection) throws Exception {
        String sql = """
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
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='食材分类表'
            """;
        
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    private void createIngredientTable(Connection connection) throws Exception {
        String sql = """
            CREATE TABLE IF NOT EXISTS `ingredients` (
                `id` bigint NOT NULL AUTO_INCREMENT COMMENT '食材ID',
                `name` varchar(100) NOT NULL COMMENT '食材名称',
                `name_en` varchar(100) DEFAULT NULL COMMENT '英文名称',
                `category` varchar(50) DEFAULT NULL COMMENT '食材分类',
                `description` varchar(500) DEFAULT NULL COMMENT '食材描述',
                `image_url` varchar(500) DEFAULT NULL COMMENT '食材图片URL',
                `calories` decimal(10,2) DEFAULT NULL COMMENT '每100g卡路里',
                `protein` decimal(10,2) DEFAULT NULL COMMENT '每100g蛋白质(g)',
                `carbs` decimal(10,2) DEFAULT NULL COMMENT '每100g碳水化合物(g)',
                `fat` decimal(10,2) DEFAULT NULL COMMENT '每100g脂肪(g)',
                `fiber` decimal(10,2) DEFAULT NULL COMMENT '每100g纤维(g)',
                `sugar` decimal(10,2) DEFAULT NULL COMMENT '每100g糖分(g)',
                `sodium` decimal(10,2) DEFAULT NULL COMMENT '每100g钠(mg)',
                `cholesterol` decimal(10,2) DEFAULT NULL COMMENT '每100g胆固醇(mg)',
                `vitamin_c` decimal(10,2) DEFAULT NULL COMMENT '每100g维生素C(mg)',
                `calcium` decimal(10,2) DEFAULT NULL COMMENT '每100g钙(mg)',
                `iron` decimal(10,2) DEFAULT NULL COMMENT '每100g铁(mg)',
                `spoonacular_id` int DEFAULT NULL COMMENT 'Spoonacular API ID',
                `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                `deleted` tinyint DEFAULT 0 COMMENT '删除标志：0-未删除，1-已删除',
                PRIMARY KEY (`id`),
                KEY `idx_name` (`name`),
                KEY `idx_spoonacular_id` (`spoonacular_id`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='食材表'
            """;
        
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    private void createUserPantryTable(Connection connection) throws Exception {
        String sql = """
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
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户食品柜表'
            """;
        
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    private void createRecipeCategoryTable(Connection connection) throws Exception {
        String sql = """
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
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='食谱分类表'
            """;
        
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    private void createRecipeTable(Connection connection) throws Exception {
        String sql = """
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
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='食谱表'
            """;
        
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    private void createRecipeIngredientTable(Connection connection) throws Exception {
        String sql = """
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
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='食谱食材关联表'
            """;
        
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    private void createMealPlanTable(Connection connection) throws Exception {
        String sql = """
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
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='膳食计划表'
            """;
        
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    private void createUserFeedbackTable(Connection connection) throws Exception {
        String sql = """
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
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户反馈表'
            """;
        
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    private void insertInitialData(Connection connection) throws Exception {
        // 插入食材分类初始数据
        String categorySql = """
            INSERT IGNORE INTO `ingredient_category` (`name`, `description`, `sort_order`) VALUES
            ('蔬菜类', '各种蔬菜', 1),
            ('肉类', '各种肉类', 2),
            ('蛋奶类', '鸡蛋、牛奶等', 3),
            ('谷物类', '大米、小麦等', 4),
            ('水果类', '各种水果', 5),
            ('豆类', '各种豆类', 6),
            ('坚果类', '各种坚果', 7),
            ('调料类', '各种调料', 8)
            """;
        
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(categorySql);
        }
        
        // 插入食谱分类初始数据
        String recipeCategorySql = """
            INSERT IGNORE INTO `recipe_category` (`name`, `description`, `sort_order`) VALUES
            ('早餐', '早餐食谱', 1),
            ('午餐', '午餐食谱', 2),
            ('晚餐', '晚餐食谱', 3),
            ('加餐', '加餐食谱', 4),
            ('汤品', '各种汤品', 5),
            ('沙拉', '各种沙拉', 6),
            ('甜品', '健康甜品', 7),
            ('饮品', '健康饮品', 8)
            """;
        
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(recipeCategorySql);
        }
        
        // 插入示例食材数据
        String ingredientSql = """
            INSERT IGNORE INTO `ingredient` (`name`, `category_id`, `calories`, `protein`, `carbohydrates`, `fat`, `fiber`, `sodium`, `vitamin_c`, `iron`, `calcium`) VALUES
            ('鸡胸肉', 2, 165.00, 31.00, 0.00, 3.60, 0.00, 74.00, 0.00, 0.70, 15.00),
            ('西兰花', 1, 34.00, 2.82, 6.64, 0.37, 2.60, 33.00, 89.20, 0.73, 47.00),
            ('鸡蛋', 3, 155.00, 13.00, 1.10, 11.00, 0.00, 124.00, 0.00, 1.20, 56.00),
            ('大米', 4, 130.00, 2.70, 28.00, 0.30, 0.40, 5.00, 0.00, 0.80, 28.00),
            ('苹果', 5, 52.00, 0.26, 13.81, 0.17, 2.40, 1.00, 4.60, 0.12, 6.00)
            """;
        
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(ingredientSql);
        }
        
        System.out.println("✅ 初始数据插入成功");
    }
}
