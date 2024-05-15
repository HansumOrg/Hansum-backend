package com.example.hansumproject.repository;


import com.example.hansumproject.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByGuesthouseGuesthouseId(Long guesthouseId);

    List<ReservationEntity> findByUserUserId(Long userId);
}
