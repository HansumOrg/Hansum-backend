package com.example.hansumproject.service;

import com.example.hansumproject.entity.RefreshEntity;
import com.example.hansumproject.jwt.JWTUtil;
import com.example.hansumproject.repository.RefreshRepository;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ReissueService {

    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public ReissueService(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    public boolean isRefreshTokenValid(String refresh) {
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return false;
        }
        return true;
    }

    public boolean isRefreshTokenAuthorized(String refresh) {
        String category = jwtUtil.getCategory(refresh);
        return category.equals("refresh") && refreshRepository.existsByRefresh(refresh);
    }

    public void deleteOldRefreshToken(String refresh) {
        refreshRepository.deleteByRefresh(refresh);
    }

    public String[] generateNewTokens(String refresh) {
        Long userId = jwtUtil.getUserId(refresh); // JWT에서 userId 추출
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccess = jwtUtil.createJwt("access", username, userId, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, userId, role, 86400000L);

        // Save new refresh token
        addRefreshEntity(userId, username, newRefresh, 86400000L);

        return new String[]{newAccess, newRefresh};
    }

    private void addRefreshEntity(Long userId, String username, String refresh, Long expiredMs) {
        Date expirationDate = new Date(System.currentTimeMillis() + expiredMs);
        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUserId(userId);
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(expirationDate.toString());
        refreshRepository.save(refreshEntity);
    }

}
