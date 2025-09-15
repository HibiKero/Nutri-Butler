package com.hibikero.nutributler.repository;

import com.hibikero.nutributler.entity.UserHealthProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户健康档案数据访问层
 */
@Repository
public interface UserHealthProfileRepository extends JpaRepository<UserHealthProfile, Long> {
    
    /**
     * 根据用户ID查找健康档案
     */
    Optional<UserHealthProfile> findByUserIdAndDeleted(Long userId, Integer deleted);
    
    /**
     * 根据用户ID和状态查找健康档案
     */
    Optional<UserHealthProfile> findByUserIdAndStatusAndDeleted(Long userId, Integer status, Integer deleted);
    
    /**
     * 根据健康目标查找档案列表
     */
    List<UserHealthProfile> findByHealthGoalAndDeleted(Integer healthGoal, Integer deleted);
    
    /**
     * 查找激活状态的健康档案
     */
    @Query("SELECT uhp FROM UserHealthProfile uhp WHERE uhp.userId = :userId AND uhp.status = 1 AND uhp.deleted = 0")
    Optional<UserHealthProfile> findActiveProfileByUserId(@Param("userId") Long userId);
    
    /**
     * 统计用户健康档案数量
     */
    @Query("SELECT COUNT(uhp) FROM UserHealthProfile uhp WHERE uhp.userId = :userId AND uhp.deleted = 0")
    Long countByUserId(@Param("userId") Long userId);
}
