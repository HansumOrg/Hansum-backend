package com.example.hansumproject.repository;

import com.example.hansumproject.entity.DibsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DibsRepository extends JpaRepository<DibsEntity, Long> {
    // 중복되는 찜이 있는지 확인
    boolean existsByUser_UserIdAndGuesthouse_GuesthouseId(Long userId, Long guesthouseId);

    // 찜 삭제
    Optional<DibsEntity> findByUser_UserIdAndGuesthouse_GuesthouseId(Long userId, Long guesthouseId);

    // 찜 찾기
    List<DibsEntity> findByUserUserId(Long userId);
}
