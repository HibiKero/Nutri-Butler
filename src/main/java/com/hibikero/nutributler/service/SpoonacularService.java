package com.hibikero.nutributler.service;

import com.hibikero.nutributler.entity.Ingredient;

import java.util.List;

/**
 * Spoonacular API服务接口
 */
public interface SpoonacularService {

    /**
     * 搜索食材
     */
    List<Ingredient> searchIngredients(String query, int number);

    /**
     * 根据ID获取食材详细信息
     */
    Ingredient getIngredientById(Integer id);

    /**
     * 批量获取食材信息
     */
    List<Ingredient> getIngredientsByIds(List<Integer> ids);

    /**
     * 初始化常用食材数据
     */
    void initializeCommonIngredients();
}
