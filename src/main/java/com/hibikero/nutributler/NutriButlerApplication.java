
package com.hibikero.nutributler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class NutriButlerApplication {

    public static void main(String[] args) {
        // 启动Spring Boot应用
        SpringApplication.run(NutriButlerApplication.class, args);
        
        // 显示访问信息
        System.out.println("✅ 系统启动完成！");
        System.out.println("🌐 前端地址: http://localhost:8080/vue-app.html");
        System.out.println("🔧 后端API: http://localhost:8080/api/");
        System.out.println("📊 测试页面: http://localhost:8080/");
    }

}

