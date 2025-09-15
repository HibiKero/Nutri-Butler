package com.hibikero.nutributler.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Spoonacular食材信息DTO
 */
@Data
public class SpoonacularIngredientDTO {
    
    private Long id;
    private String name;
    private String image;
    private String imageType;
    
    @JsonProperty("nutrition")
    private Nutrition nutrition;
    
    @Data
    public static class Nutrition {
        private List<Nutrient> nutrients;
    }
    
    @Data
    public static class Nutrient {
        private String name;
        private BigDecimal amount;
        private String unit;
        private BigDecimal percentOfDailyNeeds;
    }
    
    /**
     * 获取特定营养素的含量
     */
    public BigDecimal getNutrientAmount(String nutrientName) {
        if (nutrition == null || nutrition.nutrients == null) {
            return BigDecimal.ZERO;
        }
        
        return nutrition.nutrients.stream()
                .filter(nutrient -> nutrientName.equals(nutrient.getName()))
                .findFirst()
                .map(Nutrient::getAmount)
                .orElse(BigDecimal.ZERO);
    }
}
