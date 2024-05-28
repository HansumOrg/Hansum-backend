package com.example.hansumproject.repository;

import com.example.hansumproject.entity.FacilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacilityRepository extends JpaRepository<FacilityEntity, Long> {
    List<FacilityEntity> findByGuesthouse_GuesthouseId(Long guesthouseId);
}
