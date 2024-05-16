package com.example.hansumproject.repository;

import com.example.hansumproject.entity.StickerEntity;
import com.example.hansumproject.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StickerRepository extends JpaRepository<StickerEntity, Long> {

    Optional<StickerEntity> findByUserAndStickerText(UserEntity user, String stickerText);

    List<StickerEntity> findByUserUserId(Long userId);
}
