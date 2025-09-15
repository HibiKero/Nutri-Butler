package com.hibikero.nutributler.service;

import com.hibikero.nutributler.entity.UserPantry;

import java.time.LocalDate;
import java.util.List;

/**
 * 用户食材库存服务接口
 */
public interface UserPantryService {

    /**
     * 添加食材到库存
     */
    UserPantry addIngredientToPantry(Long userId, Long ingredientId, java.math.BigDecimal quantity, 
                                   String unit, LocalDate purchaseDate, String storageLocation, String notes);

    /**
     * 更新库存信息
     */
    UserPantry updatePantryItem(Long pantryId, java.math.BigDecimal quantity, String unit, 
                              LocalDate purchaseDate, LocalDate expiryDate, String storageLocation, String notes);

    /**
     * 标记食材为已消耗
     */
    UserPantry markAsConsumed(Long pantryId, LocalDate consumedDate);

    /**
     * 删除库存项目
     */
    void deletePantryItem(Long pantryId);

    /**
     * 根据ID获取库存项目
     */
    UserPantry getPantryItemById(Long pantryId);

    /**
     * 获取用户所有库存
     */
    List<UserPantry> getUserPantry(Long userId);

    /**
     * 获取用户未消耗的库存
     */
    List<UserPantry> getActivePantry(Long userId);

    /**
     * 获取即将过期的食材（指定天数内）
     */
    List<UserPantry> getExpiringIngredients(Long userId, int days);

    /**
     * 获取已过期的食材
     */
    List<UserPantry> getExpiredIngredients(Long userId);

    /**
     * 根据存储位置获取库存
     */
    List<UserPantry> getPantryByStorageLocation(Long userId, String storageLocation);

    /**
     * 根据食材分类获取库存
     */
    List<UserPantry> getPantryByCategory(Long userId, String category);

    /**
     * 搜索库存食材
     */
    List<UserPantry> searchPantryIngredients(Long userId, String searchTerm);

    /**
     * 获取库存统计信息
     */
    PantryStats getPantryStats(Long userId);

    /**
     * 自动计算保质期
     */
    LocalDate calculateExpiryDate(Long ingredientId, LocalDate purchaseDate);

    /**
     * 获取推荐存储位置
     */
    String getRecommendedStorageLocation(Long ingredientId);

    /**
     * 库存统计信息内部类
     */
    class PantryStats {
        private Long totalItems;
        private Long activeItems;
        private Long expiringItems;
        private Long expiredItems;
        private Long fridgeItems;
        private Long freezerItems;
        private Long roomTemperatureItems;

        // 构造方法
        public PantryStats() {}

        public PantryStats(Long totalItems, Long activeItems, Long expiringItems, Long expiredItems,
                          Long fridgeItems, Long freezerItems, Long roomTemperatureItems) {
            this.totalItems = totalItems;
            this.activeItems = activeItems;
            this.expiringItems = expiringItems;
            this.expiredItems = expiredItems;
            this.fridgeItems = fridgeItems;
            this.freezerItems = freezerItems;
            this.roomTemperatureItems = roomTemperatureItems;
        }

        // Getter和Setter方法
        public Long getTotalItems() {
            return totalItems;
        }

        public void setTotalItems(Long totalItems) {
            this.totalItems = totalItems;
        }

        public Long getActiveItems() {
            return activeItems;
        }

        public void setActiveItems(Long activeItems) {
            this.activeItems = activeItems;
        }

        public Long getExpiringItems() {
            return expiringItems;
        }

        public void setExpiringItems(Long expiringItems) {
            this.expiringItems = expiringItems;
        }

        public Long getExpiredItems() {
            return expiredItems;
        }

        public void setExpiredItems(Long expiredItems) {
            this.expiredItems = expiredItems;
        }

        public Long getFridgeItems() {
            return fridgeItems;
        }

        public void setFridgeItems(Long fridgeItems) {
            this.fridgeItems = fridgeItems;
        }

        public Long getFreezerItems() {
            return freezerItems;
        }

        public void setFreezerItems(Long freezerItems) {
            this.freezerItems = freezerItems;
        }

        public Long getRoomTemperatureItems() {
            return roomTemperatureItems;
        }

        public void setRoomTemperatureItems(Long roomTemperatureItems) {
            this.roomTemperatureItems = roomTemperatureItems;
        }
    }
}
