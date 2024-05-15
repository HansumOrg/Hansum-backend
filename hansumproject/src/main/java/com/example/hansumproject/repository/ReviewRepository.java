package com.example.hansumproject.repository;

import com.example.hansumproject.entity.GuesthouseEntity;
import com.example.hansumproject.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByGuesthouse_GuesthouseId(Long guesthouseId);

    List<ReviewEntity> findByGuesthouse(GuesthouseEntity guesthouse);
}
