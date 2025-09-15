package com.hibikero.nutributler.repository;

import com.hibikero.nutributler.entity.UserSimple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户Repository接口（简化版）
 */
@Repository
public interface UserSimpleRepository extends JpaRepository<UserSimple, Long> {

    /**
     * 根据用户名查找用户
     */
    Optional<UserSimple> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    Optional<UserSimple> findByEmail(String email);

    /**
     * 根据手机号查找用户
     */
    Optional<UserSimple> findByPhone(String phone);

    /**
     * 根据用户名和密码查找用户
     */
    Optional<UserSimple> findByUsernameAndPassword(String username, String password);

    /**
     * 查找所有正常状态的用户
     */
    @Query("SELECT u FROM UserSimple u WHERE u.status = 1 AND u.deleted = 0")
    List<UserSimple> findAllActiveUsers();

    /**
     * 根据昵称模糊查询
     */
    @Query("SELECT u FROM UserSimple u WHERE u.nickname LIKE %:nickname% AND u.deleted = 0")
    List<UserSimple> findByNicknameContaining(@Param("nickname") String nickname);
}