package com.hibikero.nutributler.controller;

import com.hibikero.nutributler.common.Result;
import com.hibikero.nutributler.entity.Ingredient;
import com.hibikero.nutributler.enums.IngredientCategory;
import com.hibikero.nutributler.repository.IngredientRepository;
import com.hibikero.nutributler.service.IngredientDataService;
import com.hibikero.nutributler.service.SpoonacularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 营养查询控制器
 */
@RestController
@RequestMapping("/api/nutrition")
public class NutritionController {

    @Autowired
    private SpoonacularService spoonacularService;

    @Autowired
    private IngredientDataService ingredientDataService;

    @Autowired
    private IngredientRepository ingredientRepository;

    /**
     * 搜索食材 - 优先从数据库查询，没有则调用API
     */
    @GetMapping("/search")
    public Result<List<Ingredient>> searchIngredients(@RequestParam String query, 
                                                     @RequestParam(defaultValue = "10") int number) {
        try {
            // 1. 首先从数据库模糊查询
            List<Ingredient> dbResults = ingredientRepository.findByNameContaining(query);
            
            if (!dbResults.isEmpty()) {
                // 如果数据库中有结果，直接返回
                return Result.success(dbResults.stream().limit(number).collect(java.util.stream.Collectors.toList()));
            }
            
            // 2. 数据库中没有结果，调用API搜索
            List<Ingredient> apiResults = spoonacularService.searchIngredients(query, number);
            
            // 3. 将API结果保存到数据库（持久化）
            for (Ingredient ingredient : apiResults) {
                try {
                    // 检查是否已存在
                    if (ingredient.getSpoonacularId() != null && 
                        ingredientRepository.findBySpoonacularId(ingredient.getSpoonacularId()).isEmpty()) {
                        ingredientRepository.save(ingredient);
                    }
                } catch (Exception e) {
                    // 忽略保存错误，继续处理其他食材
                    System.err.println("保存食材失败: " + ingredient.getName() + " - " + e.getMessage());
                }
            }
            
            return Result.success(apiResults);
        } catch (Exception e) {
            return Result.error("搜索食材失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取食材详细信息 - 优先从数据库查询，没有则调用API
     */
    @GetMapping("/ingredient/{id}")
    public Result<Ingredient> getIngredientById(@PathVariable Integer id) {
        try {
            // 1. 首先从数据库根据Spoonacular ID查询
            java.util.Optional<Ingredient> dbResult = ingredientRepository.findBySpoonacularId(id);
            if (dbResult.isPresent()) {
                Ingredient ingredient = dbResult.get();
                // 如果数据库中的食材有完整营养信息，直接返回
                if (ingredient.getCalories() != null && ingredient.getProtein() != null) {
                    return Result.success(ingredient);
                }
            }
            
            // 2. 数据库中没有或营养信息不完整，调用API查询
            Ingredient ingredient = spoonacularService.getIngredientById(id);
            if (ingredient != null) {
                // 3. 保存到数据库（持久化）
                try {
                    if (ingredient.getSpoonacularId() != null && 
                        ingredientRepository.findBySpoonacularId(ingredient.getSpoonacularId()).isEmpty()) {
                        ingredientRepository.save(ingredient);
                    }
                } catch (Exception e) {
                    System.err.println("保存食材失败: " + ingredient.getName() + " - " + e.getMessage());
                }
                
                return Result.success(ingredient);
            } else {
                return Result.error("食材不存在");
            }
        } catch (Exception e) {
            return Result.error("获取食材信息失败：" + e.getMessage());
        }
    }

    /**
     * 根据名称查询食材营养信息 - 优先从数据库查询，没有则调用API
     */
    @GetMapping("/query")
    public Result<Ingredient> queryNutrition(@RequestParam String name) {
        try {
            // 1. 首先从数据库精确查询
            java.util.Optional<Ingredient> dbResult = ingredientRepository.findByName(name);
            if (dbResult.isPresent()) {
                Ingredient ingredient = dbResult.get();
                // 如果数据库中的食材有完整营养信息，直接返回
                if (ingredient.getCalories() != null && ingredient.getProtein() != null) {
                    return Result.success(ingredient);
                }
            }
            
            // 2. 数据库中没有或营养信息不完整，调用API查询
            List<Ingredient> searchResults = spoonacularService.searchIngredients(name, 1);
            if (searchResults.isEmpty()) {
                return Result.error("未找到相关食材");
            }

            Ingredient ingredient = searchResults.get(0);
            
            // 3. 如果找到的食材有Spoonacular ID，获取详细信息
            if (ingredient.getSpoonacularId() != null) {
                Ingredient detailedIngredient = spoonacularService.getIngredientById(ingredient.getSpoonacularId());
                if (detailedIngredient != null) {
                    ingredient = detailedIngredient;
                }
            }
            
            // 4. 保存到数据库（持久化）
            try {
                if (ingredient.getSpoonacularId() != null && 
                    ingredientRepository.findBySpoonacularId(ingredient.getSpoonacularId()).isEmpty()) {
                    ingredientRepository.save(ingredient);
                }
            } catch (Exception e) {
                System.err.println("保存食材失败: " + ingredient.getName() + " - " + e.getMessage());
            }

            return Result.success(ingredient);
        } catch (Exception e) {
            return Result.error("查询营养信息失败：" + e.getMessage());
        }
    }

    /**
     * 初始化常用食材数据
     */
    @PostMapping("/init")
    public Result<String> initializeIngredients() {
        try {
            ingredientDataService.initializeCommonIngredients();
            return Result.success("食材数据初始化完成");
        } catch (Exception e) {
            return Result.error("初始化失败：" + e.getMessage());
        }
    }

    /**
     * 根据分类初始化食材
     */
    @PostMapping("/init-category")
    public Result<String> initializeIngredientsByCategory(@RequestParam String category) {
        try {
            // 这里可以根据分类参数调用不同的初始化方法
            ingredientDataService.initializeCommonIngredients();
            return Result.success("分类食材数据初始化完成");
        } catch (Exception e) {
            return Result.error("初始化失败：" + e.getMessage());
        }
    }

    /**
     * 根据分类获取食材列表
     */
    @GetMapping("/category/{category}")
    public Result<List<Ingredient>> getIngredientsByCategory(@PathVariable String category) {
        try {
            List<Ingredient> ingredients = ingredientRepository.findActiveIngredientsByCategory(category);
            return Result.success(ingredients);
        } catch (Exception e) {
            return Result.error("获取分类食材失败：" + e.getMessage());
        }
    }

    /**
     * 获取所有食材分类
     */
    @GetMapping("/categories")
    public Result<List<String>> getAllCategories() {
        try {
            List<String> categories = Arrays.asList(IngredientCategory.getAllChineseNames());
            return Result.success(categories);
        } catch (Exception e) {
            return Result.error("获取分类列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取所有食材
     */
    @GetMapping("/all")
    public Result<List<Ingredient>> getAllIngredients() {
        try {
            List<Ingredient> ingredients = ingredientRepository.findAllActiveIngredients();
            return Result.success(ingredients);
        } catch (Exception e) {
            return Result.error("获取全部食材失败：" + e.getMessage());
        }
    }
}
