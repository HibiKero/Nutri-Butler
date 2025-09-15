package com.hibikero.nutributler.controller;

import com.hibikero.nutributler.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制器
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/hello")
    public Result<String> hello() {
        return Result.success("Hello, Nutri-Butler!");
    }

    @GetMapping("/config")
    public Result<String> config() {
        return Result.success("配置测试成功！");
    }
}