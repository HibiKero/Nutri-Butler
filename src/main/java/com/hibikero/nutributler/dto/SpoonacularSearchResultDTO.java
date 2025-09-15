package com.hibikero.nutributler.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Spoonacular搜索结果DTO
 */
@Data
public class SpoonacularSearchResultDTO {
    
    private List<SearchResult> results;
    private int offset;
    private int number;
    private int totalResults;
    
    @Data
    public static class SearchResult {
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
            private java.math.BigDecimal amount;
            private String unit;
            private java.math.BigDecimal percentOfDailyNeeds;
        }
    }
}
