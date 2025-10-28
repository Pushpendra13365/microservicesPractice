package com.auth_service.repository;

import com.auth_service.entity.TokenType;
import com.auth_service.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByTokenAndBlacklistedFalse(String token);

    @Transactional
    @Modifying
    @Query("UPDATE UserToken ut SET ut.blacklisted = true WHERE ut.user.id = :userId")
    void invalidateAllUserTokens(Long userId);

    @Modifying
    @Query("UPDATE UserToken t SET t.blacklisted = true WHERE t.token = :token AND t.blacklisted = false")
    @SuppressWarnings("UnusedReturnValue")
    int invalidateToken(@Param("token") String token);

    @Query("SELECT ut.user.id FROM UserToken ut WHERE ut.token = :token AND ut.blacklisted = false")
    Optional<Long> findUserIdByToken(@Param("token") String token);

}
