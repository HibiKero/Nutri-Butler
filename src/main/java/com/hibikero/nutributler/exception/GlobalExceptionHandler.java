package com.hibikero.nutributler.exception;

import com.hibikero.nutributler.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException e) {
        return Result.error(e.getMessage());
    }
    
    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return Result.error("参数错误：" + e.getMessage());
    }
    
    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        return Result.error("系统异常：" + e.getMessage());
    }
}
