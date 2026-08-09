package com.finpilot.security.service;

import com.finpilot.entity.RefreshToken;
import com.finpilot.entity.User;
import com.finpilot.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpiration;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(User user) {

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken.setExpiryDate(
                LocalDateTime.now()
                        .plusSeconds(refreshTokenExpiration / 1000)
        );

        refreshToken.setUser(user);

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {

        if (refreshToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            refreshTokenRepository.delete(refreshToken);

            throw new RuntimeException(
                    "Refresh token has expired. Please login again."
            );
        }

        return refreshToken;
    }

    public RefreshToken findByToken(String token) {

        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Refresh token not found."
                        )
                );
    }

    public void deleteByUserId(Long userId) {

        refreshTokenRepository.deleteByUserId(userId);
    }
}