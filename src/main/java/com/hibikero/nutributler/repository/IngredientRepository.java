package com.hibikero.nutributler.repository;

import com.hibikero.nutributler.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 食材Repository接口
 */
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    /**
     * 根据名称查找食材
     */
    Optional<Ingredient> findByName(String name);

    /**
     * 根据英文名称查找食材
     */
    Optional<Ingredient> findByNameEn(String nameEn);

    /**
     * 根据Spoonacular ID查找食材
     */
    Optional<Ingredient> findBySpoonacularId(Integer spoonacularId);

    /**
     * 根据分类查找食材
     */
    List<Ingredient> findByCategory(String category);

    /**
     * 根据名称模糊查询
     */
    @Query("SELECT i FROM Ingredient i WHERE i.name LIKE %:name% AND i.deleted = 0")
    List<Ingredient> findByNameContaining(@Param("name") String name);

    /**
     * 查找所有正常状态的食材
     */
    @Query("SELECT i FROM Ingredient i WHERE i.deleted = 0 ORDER BY i.name")
    List<Ingredient> findAllActiveIngredients();

    /**
     * 根据分类查找正常状态的食材
     */
    @Query("SELECT i FROM Ingredient i WHERE i.category = :category AND i.deleted = 0 ORDER BY i.name")
    List<Ingredient> findActiveIngredientsByCategory(@Param("category") String category);
}
