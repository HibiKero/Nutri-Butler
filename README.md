# 营养管家 (Nutri-Butler)

一个完整的智能营养管理系统，包含后端API服务和前端Web应用，提供用户管理、健康档案、营养计算、个性化建议等功能。

## 项目简介

营养管家是一个基于Spring Boot的智能健康管理平台，旨在帮助用户通过科学的营养策略和个性化的膳食规划来管理身材和改善健康状况。

## 技术栈

### 后端技术
- **框架**: Spring Boot 3.2.5
- **数据库**: MySQL 8.0
- **ORM框架**: Spring Data JPA
- **缓存**: Redis
- **定时任务**: Spring Quartz
- **构建工具**: Maven
- **Java版本**: 17

### 前端技术
- **框架**: Vue 3
- **构建工具**: Vite
- **UI组件库**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router
- **HTTP客户端**: Axios

## 核心功能模块

### 模块一：用户健康画像与目标策略模块
- 多维度健康评估问卷
- 个性化营养策略生成
- 目标卡路里计算
- 营养比例推荐

### 模块二：智能食品柜与库存管理模块
- 食材库存管理
- 保质期预警
- 库存状态自动更新
- 食材搜索功能

### 模块三：AI膳食规划与生成引擎
- 智能食谱推荐
- 完美匹配方案
- 一步之遥方案
- 灵活替换建议

### 模块四：食谱库与用户共享食谱模块
- 食谱CRUD操作
- 食谱分类管理
- 健康标签搜索
- 用户食谱分享

### 模块五：数据追踪与反馈闭环模块
- 用户反馈收集
- 健康趋势分析
- 个性化建议生成
- 营养策略动态调整

## 项目结构

```
Nutri-Butler/
├── src/                    # 后端Spring Boot项目
│   ├── main/java/         # Java源码
│   └── main/resources/    # 配置文件
├── frontend/              # 前端Vue项目
│   ├── src/              # Vue源码
│   ├── package.json      # 前端依赖
│   └── vite.config.js    # Vite配置
└── README.md             # 项目说明
```

### 后端结构
```
src/main/java/com/hibikero/nutributler/
├── common/                 # 通用类
├── config/                # 配置类
├── controller/            # REST控制器
├── entity/                # 实体类
├── repository/            # 数据访问层
├── service/               # 业务逻辑层
└── task/                  # 定时任务
```

### 前端结构
```
frontend/src/
├── components/            # 公共组件
├── views/                # 页面组件
├── router/               # 路由配置
├── stores/               # 状态管理
├── utils/                # 工具函数
└── main.js               # 入口文件
```

## 快速开始

### 环境要求
- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Redis 6.0+
- Maven 3.6+

### 安装步骤

1. **克隆项目**
```bash
git clone <repository-url>
cd Nutri-Butler
```

2. **配置数据库**
项目已配置自动数据库初始化，无需手动创建数据库。应用启动时会自动：
- 检测并创建 `nutri_butler` 数据库
- 创建所有必要的表结构
- 插入初始数据

3. **修改配置**
```properties
# 修改 src/main/resources/application.properties
spring.datasource.username=root
spring.datasource.password=123456
spring.data.redis.host=localhost
spoonacular.api.key=your_api_key
```

4. **启动后端**
```bash
mvn spring-boot:run
```
后端服务地址: http://localhost:8080

5. **启动前端**
```bash
cd frontend
npm install
npm run dev
```
前端地址: http://localhost:3000

6. **一键启动**
- Windows: 运行 `start.bat`
- Linux/Mac: 运行 `./start.sh`

## API接口文档

### 系统监控
- `GET /api/test/hello` - 基础健康检查

### 用户管理
- `GET /api/user-simple` - 获取所有用户
- `POST /api/user-simple` - 创建用户
- `GET /api/user-simple/{id}` - 获取用户详情
- `PUT /api/user-simple/{id}` - 更新用户信息
- `DELETE /api/user-simple/{id}` - 删除用户

## 特色功能

### 自动数据库初始化
- 应用启动时自动检测并创建数据库
- 自动创建表结构和插入初始数据
- 无需手动配置数据库

### Spoonacular API集成
- 超过100,000种食物的营养数据
- 详细的营养成分信息（卡路里、维生素、矿物质等）
- 自动数据同步和更新
- 高质量的食物图片和描述

### 智能推荐算法
- 基于用户健康档案的个性化推荐
- 考虑库存约束的食谱匹配
- 多维度评分系统

### 实时监控
- 食材保质期自动预警
- 库存状态实时更新
- 健康趋势分析

## 开发计划

- [ ] 完善实体类和业务逻辑
- [ ] 实现完整的API接口
- [ ] 前端界面开发
- [ ] 移动端适配
- [ ] 图像识别功能
- [ ] 社交分享功能
- [ ] 数据可视化

## 许可证

本项目采用 MIT 许可证

## 联系方式

如有问题或建议，请通过以下方式联系：
- 邮箱: your-email@example.com
- 项目地址: https://github.com/your-username/Nutri-Butler