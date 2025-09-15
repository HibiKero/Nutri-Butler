package com.hibikero.nutributler.service.impl;

import com.hibikero.nutributler.entity.UserPantry;
import com.hibikero.nutributler.entity.Ingredient;
import com.hibikero.nutributler.enums.ExpiryRule;
import com.hibikero.nutributler.repository.UserPantryRepository;
import com.hibikero.nutributler.repository.IngredientRepository;
import com.hibikero.nutributler.service.UserPantryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 用户食材库存服务实现类
 */
@Service
@Transactional
public class UserPantryServiceImpl implements UserPantryService {

    @Autowired
    private UserPantryRepository userPantryRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    public UserPantry addIngredientToPantry(Long userId, Long ingredientId, java.math.BigDecimal quantity, 
                                          String unit, LocalDate purchaseDate, String storageLocation, String notes) {
        // 验证食材是否存在
        Optional<Ingredient> ingredientOpt = ingredientRepository.findById(ingredientId);
        if (ingredientOpt.isEmpty()) {
            throw new IllegalArgumentException("食材不存在");
        }
        
        Ingredient ingredient = ingredientOpt.get();
        
        // 自动计算保质期
        LocalDate expiryDate = calculateExpiryDate(ingredientId, purchaseDate);
        
        // 如果没有指定存储位置，使用推荐位置
        if (storageLocation == null || storageLocation.trim().isEmpty()) {
            storageLocation = getRecommendedStorageLocation(ingredientId);
        }

        UserPantry pantryItem = new UserPantry();
        pantryItem.setUserId(userId);
        pantryItem.setIngredientId(ingredientId);
        pantryItem.setIngredientName(ingredient.getName()); // 设置食材名称
        pantryItem.setQuantity(quantity);
        pantryItem.setUnit(unit);
        pantryItem.setPurchaseDate(purchaseDate);
        pantryItem.setExpiryDate(expiryDate);
        pantryItem.setStorageLocation(storageLocation);
        pantryItem.setNotes(notes);
        pantryItem.setIsConsumed(false);

        return userPantryRepository.save(pantryItem);
    }

    @Override
    public UserPantry updatePantryItem(Long pantryId, java.math.BigDecimal quantity, String unit, 
                                     LocalDate purchaseDate, LocalDate expiryDate, String storageLocation, String notes) {
        Optional<UserPantry> pantryOpt = userPantryRepository.findById(pantryId);
        if (pantryOpt.isEmpty()) {
            throw new IllegalArgumentException("库存项目不存在");
        }

        UserPantry pantryItem = pantryOpt.get();
        pantryItem.setQuantity(quantity);
        pantryItem.setUnit(unit);
        pantryItem.setPurchaseDate(purchaseDate);
        pantryItem.setExpiryDate(expiryDate);
        pantryItem.setStorageLocation(storageLocation);
        pantryItem.setNotes(notes);

        return userPantryRepository.save(pantryItem);
    }

    @Override
    public UserPantry markAsConsumed(Long pantryId, LocalDate consumedDate) {
        Optional<UserPantry> pantryOpt = userPantryRepository.findById(pantryId);
        if (pantryOpt.isEmpty()) {
            throw new IllegalArgumentException("库存项目不存在");
        }

        UserPantry pantryItem = pantryOpt.get();
        pantryItem.setIsConsumed(true);
        pantryItem.setConsumedDate(consumedDate != null ? consumedDate : LocalDate.now());

        return userPantryRepository.save(pantryItem);
    }

    @Override
    public void deletePantryItem(Long pantryId) {
        Optional<UserPantry> pantryOpt = userPantryRepository.findById(pantryId);
        if (pantryOpt.isEmpty()) {
            throw new IllegalArgumentException("库存项目不存在");
        }

        UserPantry pantryItem = pantryOpt.get();
        pantryItem.setDeleted(true);
        userPantryRepository.save(pantryItem);
    }

    @Override
    public UserPantry getPantryItemById(Long pantryId) {
        return userPantryRepository.findById(pantryId).orElse(null);
    }

    @Override
    public List<UserPantry> getUserPantry(Long userId) {
        List<UserPantry> pantryItems = userPantryRepository.findByUserIdAndDeletedFalseOrderByPurchaseDateDesc(userId);
        
        // 加载关联的食材信息
        for (UserPantry item : pantryItems) {
            Optional<Ingredient> ingredient = ingredientRepository.findById(item.getIngredientId());
            ingredient.ifPresent(item::setIngredient);
        }
        
        return pantryItems;
    }

    @Override
    public List<UserPantry> getActivePantry(Long userId) {
        List<UserPantry> pantryItems = userPantryRepository.findByUserIdAndIsConsumedAndDeletedFalseOrderByExpiryDateAsc(userId, false);
        
        // 加载关联的食材信息
        for (UserPantry item : pantryItems) {
            Optional<Ingredient> ingredient = ingredientRepository.findById(item.getIngredientId());
            ingredient.ifPresent(item::setIngredient);
        }
        
        return pantryItems;
    }

    @Override
    public List<UserPantry> getExpiringIngredients(Long userId, int days) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(days);
        
        List<UserPantry> pantryItems = userPantryRepository.findExpiringIngredients(userId, startDate, endDate);
        
        // 加载关联的食材信息
        for (UserPantry item : pantryItems) {
            Optional<Ingredient> ingredient = ingredientRepository.findById(item.getIngredientId());
            ingredient.ifPresent(item::setIngredient);
        }
        
        return pantryItems;
    }

    @Override
    public List<UserPantry> getExpiredIngredients(Long userId) {
        List<UserPantry> pantryItems = userPantryRepository.findExpiredIngredients(userId, LocalDate.now());
        
        // 加载关联的食材信息
        for (UserPantry item : pantryItems) {
            Optional<Ingredient> ingredient = ingredientRepository.findById(item.getIngredientId());
            ingredient.ifPresent(item::setIngredient);
        }
        
        return pantryItems;
    }

    @Override
    public List<UserPantry> getPantryByStorageLocation(Long userId, String storageLocation) {
        List<UserPantry> pantryItems = userPantryRepository.findByUserIdAndStorageLocationAndDeletedFalseOrderByExpiryDateAsc(userId, storageLocation);
        
        // 加载关联的食材信息
        for (UserPantry item : pantryItems) {
            Optional<Ingredient> ingredient = ingredientRepository.findById(item.getIngredientId());
            ingredient.ifPresent(item::setIngredient);
        }
        
        return pantryItems;
    }

    @Override
    public List<UserPantry> getPantryByCategory(Long userId, String category) {
        List<UserPantry> pantryItems = userPantryRepository.findByUserIdAndIngredientCategory(userId, category);
        
        // 加载关联的食材信息
        for (UserPantry item : pantryItems) {
            Optional<Ingredient> ingredient = ingredientRepository.findById(item.getIngredientId());
            ingredient.ifPresent(item::setIngredient);
        }
        
        return pantryItems;
    }

    @Override
    public List<UserPantry> searchPantryIngredients(Long userId, String searchTerm) {
        List<UserPantry> pantryItems = userPantryRepository.findByUserIdAndIngredientNameContaining(userId, searchTerm);
        
        // 加载关联的食材信息
        for (UserPantry item : pantryItems) {
            Optional<Ingredient> ingredient = ingredientRepository.findById(item.getIngredientId());
            ingredient.ifPresent(item::setIngredient);
        }
        
        return pantryItems;
    }

    @Override
    public PantryStats getPantryStats(Long userId) {
        Long totalItems = userPantryRepository.countActiveIngredientsByUserId(userId);
        Long activeItems = (long) userPantryRepository.findByUserIdAndIsConsumedAndDeletedFalseOrderByExpiryDateAsc(userId, false).size();
        
        // 即将过期的食材（7天内）
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        Long expiringItems = userPantryRepository.countExpiringIngredients(userId, startDate, endDate);
        
        // 已过期的食材
        Long expiredItems = userPantryRepository.countExpiredIngredients(userId, LocalDate.now());
        
        // 按存储位置统计
        Long fridgeItems = (long) userPantryRepository.findByUserIdAndStorageLocationAndDeletedFalseOrderByExpiryDateAsc(userId, "冷藏").size();
        Long freezerItems = (long) userPantryRepository.findByUserIdAndStorageLocationAndDeletedFalseOrderByExpiryDateAsc(userId, "冷冻").size();
        Long roomTemperatureItems = (long) userPantryRepository.findByUserIdAndStorageLocationAndDeletedFalseOrderByExpiryDateAsc(userId, "常温").size();

        return new PantryStats(totalItems, activeItems, expiringItems, expiredItems, 
                              fridgeItems, freezerItems, roomTemperatureItems);
    }

    @Override
    public LocalDate calculateExpiryDate(Long ingredientId, LocalDate purchaseDate) {
        Optional<Ingredient> ingredientOpt = ingredientRepository.findById(ingredientId);
        if (ingredientOpt.isEmpty()) {
            return purchaseDate.plusDays(7); // 默认7天
        }

        Ingredient ingredient = ingredientOpt.get();
        int expiryDays = ExpiryRule.getExpiryDays(ingredient.getName());
        return purchaseDate.plusDays(expiryDays);
    }

    @Override
    public String getRecommendedStorageLocation(Long ingredientId) {
        Optional<Ingredient> ingredientOpt = ingredientRepository.findById(ingredientId);
        if (ingredientOpt.isEmpty()) {
            return "冷藏"; // 默认冷藏
        }

        Ingredient ingredient = ingredientOpt.get();
        return ExpiryRule.getStorageMethod(ingredient.getName());
    }
}
