package com.hibikero.nutributler.controller;

import com.hibikero.nutributler.common.Result;
import com.hibikero.nutributler.entity.UserPantry;
import com.hibikero.nutributler.service.UserPantryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 用户食材库存控制器
 */
@RestController
@RequestMapping("/api/pantry")
public class UserPantryController {

    @Autowired
    private UserPantryService userPantryService;

    /**
     * 添加食材到库存
     */
    @PostMapping("/add")
    public Result<UserPantry> addIngredientToPantry(@RequestParam Long userId,
                                                   @RequestParam Long ingredientId,
                                                   @RequestParam java.math.BigDecimal quantity,
                                                   @RequestParam String unit,
                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate purchaseDate,
                                                   @RequestParam(required = false) String storageLocation,
                                                   @RequestParam(required = false) String notes) {
        try {
            UserPantry pantryItem = userPantryService.addIngredientToPantry(
                userId, ingredientId, quantity, unit, purchaseDate, storageLocation, notes);
            return Result.success(pantryItem);
        } catch (Exception e) {
            return Result.error("添加食材到库存失败：" + e.getMessage());
        }
    }

    /**
     * 更新库存信息
     */
    @PutMapping("/update/{pantryId}")
    public Result<UserPantry> updatePantryItem(@PathVariable Long pantryId,
                                             @RequestParam java.math.BigDecimal quantity,
                                             @RequestParam String unit,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate purchaseDate,
                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate,
                                             @RequestParam(required = false) String storageLocation,
                                             @RequestParam(required = false) String notes) {
        try {
            UserPantry pantryItem = userPantryService.updatePantryItem(
                pantryId, quantity, unit, purchaseDate, expiryDate, storageLocation, notes);
            return Result.success(pantryItem);
        } catch (Exception e) {
            return Result.error("更新库存信息失败：" + e.getMessage());
        }
    }

    /**
     * 标记食材为已消耗
     */
    @PutMapping("/consume/{pantryId}")
    public Result<UserPantry> markAsConsumed(@PathVariable Long pantryId,
                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate consumedDate) {
        try {
            UserPantry pantryItem = userPantryService.markAsConsumed(pantryId, consumedDate);
            return Result.success(pantryItem);
        } catch (Exception e) {
            return Result.error("标记食材为已消耗失败：" + e.getMessage());
        }
    }

    /**
     * 删除库存项目
     */
    @DeleteMapping("/delete/{pantryId}")
    public Result<String> deletePantryItem(@PathVariable Long pantryId) {
        try {
            userPantryService.deletePantryItem(pantryId);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error("删除库存项目失败：" + e.getMessage());
        }
    }

    /**
     * 获取库存项目详情
     */
    @GetMapping("/item/{pantryId}")
    public Result<UserPantry> getPantryItemById(@PathVariable Long pantryId) {
        try {
            UserPantry pantryItem = userPantryService.getPantryItemById(pantryId);
            if (pantryItem != null) {
                return Result.success(pantryItem);
            } else {
                return Result.error("库存项目不存在");
            }
        } catch (Exception e) {
            return Result.error("获取库存项目失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户所有库存
     */
    @GetMapping("/user/{userId}")
    public Result<List<UserPantry>> getUserPantry(@PathVariable Long userId) {
        try {
            List<UserPantry> pantryItems = userPantryService.getUserPantry(userId);
            return Result.success(pantryItems);
        } catch (Exception e) {
            return Result.error("获取用户库存失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户未消耗的库存
     */
    @GetMapping("/active/{userId}")
    public Result<List<UserPantry>> getActivePantry(@PathVariable Long userId) {
        try {
            List<UserPantry> pantryItems = userPantryService.getActivePantry(userId);
            return Result.success(pantryItems);
        } catch (Exception e) {
            return Result.error("获取活跃库存失败：" + e.getMessage());
        }
    }

    /**
     * 获取即将过期的食材
     */
    @GetMapping("/expiring/{userId}")
    public Result<List<UserPantry>> getExpiringIngredients(@PathVariable Long userId,
                                                          @RequestParam(defaultValue = "7") int days) {
        try {
            List<UserPantry> pantryItems = userPantryService.getExpiringIngredients(userId, days);
            return Result.success(pantryItems);
        } catch (Exception e) {
            return Result.error("获取即将过期食材失败：" + e.getMessage());
        }
    }

    /**
     * 获取已过期的食材
     */
    @GetMapping("/expired/{userId}")
    public Result<List<UserPantry>> getExpiredIngredients(@PathVariable Long userId) {
        try {
            List<UserPantry> pantryItems = userPantryService.getExpiredIngredients(userId);
            return Result.success(pantryItems);
        } catch (Exception e) {
            return Result.error("获取已过期食材失败：" + e.getMessage());
        }
    }

    /**
     * 根据存储位置获取库存
     */
    @GetMapping("/storage/{userId}")
    public Result<List<UserPantry>> getPantryByStorageLocation(@PathVariable Long userId,
                                                              @RequestParam String storageLocation) {
        try {
            List<UserPantry> pantryItems = userPantryService.getPantryByStorageLocation(userId, storageLocation);
            return Result.success(pantryItems);
        } catch (Exception e) {
            return Result.error("获取存储位置库存失败：" + e.getMessage());
        }
    }

    /**
     * 根据食材分类获取库存
     */
    @GetMapping("/category/{userId}")
    public Result<List<UserPantry>> getPantryByCategory(@PathVariable Long userId,
                                                       @RequestParam String category) {
        try {
            List<UserPantry> pantryItems = userPantryService.getPantryByCategory(userId, category);
            return Result.success(pantryItems);
        } catch (Exception e) {
            return Result.error("获取分类库存失败：" + e.getMessage());
        }
    }

    /**
     * 搜索库存食材
     */
    @GetMapping("/search/{userId}")
    public Result<List<UserPantry>> searchPantryIngredients(@PathVariable Long userId,
                                                           @RequestParam String searchTerm) {
        try {
            List<UserPantry> pantryItems = userPantryService.searchPantryIngredients(userId, searchTerm);
            return Result.success(pantryItems);
        } catch (Exception e) {
            return Result.error("搜索库存食材失败：" + e.getMessage());
        }
    }

    /**
     * 获取库存统计信息
     */
    @GetMapping("/stats/{userId}")
    public Result<UserPantryService.PantryStats> getPantryStats(@PathVariable Long userId) {
        try {
            UserPantryService.PantryStats stats = userPantryService.getPantryStats(userId);
            return Result.success(stats);
        } catch (Exception e) {
            return Result.error("获取库存统计失败：" + e.getMessage());
        }
    }

    /**
     * 计算保质期
     */
    @GetMapping("/calculate-expiry")
    public Result<LocalDate> calculateExpiryDate(@RequestParam Long ingredientId,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate purchaseDate) {
        try {
            LocalDate expiryDate = userPantryService.calculateExpiryDate(ingredientId, purchaseDate);
            return Result.success(expiryDate);
        } catch (Exception e) {
            return Result.error("计算保质期失败：" + e.getMessage());
        }
    }

    /**
     * 获取推荐存储位置
     */
    @GetMapping("/recommended-storage")
    public Result<String> getRecommendedStorageLocation(@RequestParam Long ingredientId) {
        try {
            String storageLocation = userPantryService.getRecommendedStorageLocation(ingredientId);
            return Result.success(storageLocation);
        } catch (Exception e) {
            return Result.error("获取推荐存储位置失败：" + e.getMessage());
        }
    }
}
