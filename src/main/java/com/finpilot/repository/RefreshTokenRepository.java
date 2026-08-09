package com.finpilot.repository;
import org.springframework.transaction.annotation.Transactional;
import com.finpilot.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    @Transactional
    void deleteByUserId(Long userId);
}