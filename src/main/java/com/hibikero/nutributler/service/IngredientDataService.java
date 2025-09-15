package com.hibikero.nutributler.service;

import com.hibikero.nutributler.entity.Ingredient;

import java.util.List;

/**
 * 食材数据服务接口
 */
public interface IngredientDataService {

    /**
     * 初始化常用食材数据
     */
    void initializeCommonIngredients();

    /**
     * 根据分类初始化食材
     */
    void initializeIngredientsByCategory(String category, List<String[]> ingredients);

    /**
     * 搜索并保存食材
     */
    Ingredient searchAndSaveIngredient(String chineseName, String englishName, String category);
}
