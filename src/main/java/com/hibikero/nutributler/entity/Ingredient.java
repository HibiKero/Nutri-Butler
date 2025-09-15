package com.hibikero.nutributler.entity;

import com.hibikero.nutributler.common.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * 食材实体类
 */
@Entity
@Table(name = "ingredients")
public class Ingredient extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "name_en", length = 100)
    private String nameEn;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    // 营养成分 (每100g)
    @Column(name = "calories", precision = 10, scale = 2)
    private BigDecimal calories;

    @Column(name = "protein", precision = 10, scale = 2)
    private BigDecimal protein;

    @Column(name = "carbs", precision = 10, scale = 2)
    private BigDecimal carbs;

    @Column(name = "fat", precision = 10, scale = 2)
    private BigDecimal fat;

    @Column(name = "fiber", precision = 10, scale = 2)
    private BigDecimal fiber;

    @Column(name = "sugar", precision = 10, scale = 2)
    private BigDecimal sugar;

    @Column(name = "sodium", precision = 10, scale = 2)
    private BigDecimal sodium;

    @Column(name = "cholesterol", precision = 10, scale = 2)
    private BigDecimal cholesterol;

    @Column(name = "vitamin_c", precision = 10, scale = 2)
    private BigDecimal vitaminC;

    @Column(name = "calcium", precision = 10, scale = 2)
    private BigDecimal calcium;

    @Column(name = "iron", precision = 10, scale = 2)
    private BigDecimal iron;

    @Column(name = "spoonacular_id")
    private Integer spoonacularId;

    // 构造方法
    public Ingredient() {}

    public Ingredient(String name, String category) {
        this.name = name;
        this.category = category;
    }

    // Getter和Setter方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getCalories() {
        return calories;
    }

    public void setCalories(BigDecimal calories) {
        this.calories = calories;
    }

    public BigDecimal getProtein() {
        return protein;
    }

    public void setProtein(BigDecimal protein) {
        this.protein = protein;
    }

    public BigDecimal getCarbs() {
        return carbs;
    }

    public void setCarbs(BigDecimal carbs) {
        this.carbs = carbs;
    }

    public BigDecimal getFat() {
        return fat;
    }

    public void setFat(BigDecimal fat) {
        this.fat = fat;
    }

    public BigDecimal getFiber() {
        return fiber;
    }

    public void setFiber(BigDecimal fiber) {
        this.fiber = fiber;
    }

    public BigDecimal getSugar() {
        return sugar;
    }

    public void setSugar(BigDecimal sugar) {
        this.sugar = sugar;
    }

    public BigDecimal getSodium() {
        return sodium;
    }

    public void setSodium(BigDecimal sodium) {
        this.sodium = sodium;
    }

    public BigDecimal getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(BigDecimal cholesterol) {
        this.cholesterol = cholesterol;
    }

    public BigDecimal getVitaminC() {
        return vitaminC;
    }

    public void setVitaminC(BigDecimal vitaminC) {
        this.vitaminC = vitaminC;
    }

    public BigDecimal getCalcium() {
        return calcium;
    }

    public void setCalcium(BigDecimal calcium) {
        this.calcium = calcium;
    }

    public BigDecimal getIron() {
        return iron;
    }

    public void setIron(BigDecimal iron) {
        this.iron = iron;
    }

    public Integer getSpoonacularId() {
        return spoonacularId;
    }

    public void setSpoonacularId(Integer spoonacularId) {
        this.spoonacularId = spoonacularId;
    }
}
