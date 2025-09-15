package com.hibikero.nutributler.repository;

import com.hibikero.nutributler.entity.UserPantry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 用户食材库存Repository接口
 */
@Repository
public interface UserPantryRepository extends JpaRepository<UserPantry, Long> {

    /**
     * 根据用户ID查找所有库存
     */
    List<UserPantry> findByUserIdAndDeletedFalseOrderByPurchaseDateDesc(Long userId);

    /**
     * 根据用户ID和食材ID查找库存
     */
    List<UserPantry> findByUserIdAndIngredientIdAndDeletedFalse(Long userId, Long ingredientId);

    /**
     * 根据用户ID和是否已消耗查找库存
     */
    List<UserPantry> findByUserIdAndIsConsumedAndDeletedFalseOrderByExpiryDateAsc(Long userId, Boolean isConsumed);

    /**
     * 查找即将过期的食材（指定天数内）
     */
    @Query("SELECT up FROM UserPantry up WHERE up.userId = :userId AND up.isConsumed = false AND up.deleted = false AND up.expiryDate BETWEEN :startDate AND :endDate ORDER BY up.expiryDate ASC")
    List<UserPantry> findExpiringIngredients(@Param("userId") Long userId, 
                                           @Param("startDate") LocalDate startDate, 
                                           @Param("endDate") LocalDate endDate);

    /**
     * 查找已过期的食材
     */
    @Query("SELECT up FROM UserPantry up WHERE up.userId = :userId AND up.isConsumed = false AND up.deleted = false AND up.expiryDate < :currentDate ORDER BY up.expiryDate ASC")
    List<UserPantry> findExpiredIngredients(@Param("userId") Long userId, @Param("currentDate") LocalDate currentDate);

    /**
     * 根据存储位置查找库存
     */
    List<UserPantry> findByUserIdAndStorageLocationAndDeletedFalseOrderByExpiryDateAsc(Long userId, String storageLocation);

    /**
     * 根据食材分类查找库存 - 需要先通过ingredientId关联查询
     */
    @Query("SELECT up FROM UserPantry up WHERE up.userId = :userId AND up.isConsumed = false AND up.deleted = false AND up.ingredientId IN (SELECT i.id FROM Ingredient i WHERE i.category = :category) ORDER BY up.expiryDate ASC")
    List<UserPantry> findByUserIdAndIngredientCategory(@Param("userId") Long userId, @Param("category") String category);

    /**
     * 统计用户库存数量
     */
    @Query("SELECT COUNT(up) FROM UserPantry up WHERE up.userId = :userId AND up.isConsumed = false AND up.deleted = false")
    Long countActiveIngredientsByUserId(@Param("userId") Long userId);

    /**
     * 统计即将过期的食材数量
     */
    @Query("SELECT COUNT(up) FROM UserPantry up WHERE up.userId = :userId AND up.isConsumed = false AND up.deleted = false AND up.expiryDate BETWEEN :startDate AND :endDate")
    Long countExpiringIngredients(@Param("userId") Long userId, 
                                @Param("startDate") LocalDate startDate, 
                                @Param("endDate") LocalDate endDate);

    /**
     * 统计已过期的食材数量
     */
    @Query("SELECT COUNT(up) FROM UserPantry up WHERE up.userId = :userId AND up.isConsumed = false AND up.deleted = false AND up.expiryDate < :currentDate")
    Long countExpiredIngredients(@Param("userId") Long userId, @Param("currentDate") LocalDate currentDate);

    /**
     * 查找用户最近添加的食材
     */
    @Query("SELECT up FROM UserPantry up WHERE up.userId = :userId AND up.deleted = false ORDER BY up.createdAt DESC")
    List<UserPantry> findRecentIngredients(@Param("userId") Long userId, org.springframework.data.domain.Pageable pageable);

    /**
     * 根据食材名称模糊查询库存 - 需要先通过ingredientId关联查询
     */
    @Query("SELECT up FROM UserPantry up WHERE up.userId = :userId AND up.isConsumed = false AND up.deleted = false AND up.ingredientId IN (SELECT i.id FROM Ingredient i WHERE i.name LIKE %:name% OR i.nameEn LIKE %:name%) ORDER BY up.expiryDate ASC")
    List<UserPantry> findByUserIdAndIngredientNameContaining(@Param("userId") Long userId, @Param("name") String name);
}
