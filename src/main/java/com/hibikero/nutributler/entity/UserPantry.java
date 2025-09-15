package com.hibikero.nutributler.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户食材库存实体
 */
@Entity
@Table(name = "user_pantry")
public class UserPantry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "ingredient_id", nullable = false)
    private Long ingredientId;

    @Column(name = "ingredient_name", nullable = false, length = 100)
    private String ingredientName; // 食材名称（冗余字段）

    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unit", nullable = false, length = 20)
    private String unit; // 单位：个、斤、克、毫升等

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "storage_location", length = 50)
    private String storageLocation; // 存储位置：冰箱、冷冻、常温等

    @Column(name = "notes", length = 500)
    private String notes; // 备注

    @Column(name = "is_consumed", nullable = false)
    private Boolean isConsumed = false; // 是否已消耗

    @Column(name = "consumed_date")
    private LocalDate consumedDate; // 消耗日期

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted")
    private Boolean deleted = false;

    // 关联食材信息（非数据库字段）
    @Transient
    private Ingredient ingredient;

    // 构造方法
    public UserPantry() {}

    public UserPantry(Long userId, Long ingredientId, String ingredientName, BigDecimal quantity, String unit, 
                     LocalDate purchaseDate, LocalDate expiryDate, String storageLocation) {
        this.userId = userId;
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.unit = unit;
        this.purchaseDate = purchaseDate;
        this.expiryDate = expiryDate;
        this.storageLocation = storageLocation;
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsConsumed() {
        return isConsumed;
    }

    public void setIsConsumed(Boolean isConsumed) {
        this.isConsumed = isConsumed;
    }

    public LocalDate getConsumedDate() {
        return consumedDate;
    }

    public void setConsumedDate(LocalDate consumedDate) {
        this.consumedDate = consumedDate;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * 计算剩余保质期天数
     */
    public int getRemainingDays() {
        if (expiryDate == null) {
            return -1; // 无保质期信息
        }
        return (int) java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }

    /**
     * 获取保质期状态
     */
    public String getExpiryStatus() {
        int remainingDays = getRemainingDays();
        if (remainingDays < 0) {
            return "unknown"; // 无保质期信息
        } else if (remainingDays <= 0) {
            return "expired"; // 已过期
        } else if (remainingDays <= 1) {
            return "critical"; // 今天过期
        } else if (remainingDays <= 3) {
            return "warning"; // 3天内过期
        } else if (remainingDays <= 7) {
            return "caution"; // 一周内过期
        } else {
            return "good"; // 正常
        }
    }

    /**
     * 获取保质期状态颜色
     */
    public String getExpiryColor() {
        String status = getExpiryStatus();
        switch (status) {
            case "expired":
                return "#ff4444"; // 红色 - 已过期
            case "critical":
                return "#ff8800"; // 橙色 - 今天过期
            case "warning":
                return "#ffaa00"; // 黄色 - 3天内过期
            case "caution":
                return "#ffcc00"; // 浅黄色 - 一周内过期
            case "good":
                return "#44ff44"; // 绿色 - 正常
            default:
                return "#888888"; // 灰色 - 无保质期信息
        }
    }
}
