package com.example.hansumproject.repository;

import com.example.hansumproject.entity.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

    // refresh token 중복 확인
    Boolean existsByRefresh(String refresh);

    // refresh token 삭제 메서드
    @Transactional
    void deleteByRefresh(String refresh);
}
