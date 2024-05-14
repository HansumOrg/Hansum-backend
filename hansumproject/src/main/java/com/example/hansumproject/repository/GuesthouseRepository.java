package com.example.hansumproject.repository;

import com.example.hansumproject.entity.GuesthouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuesthouseRepository extends JpaRepository<GuesthouseEntity, Long> {
    GuesthouseEntity findByImageUrlContaining(String fileName);

    List<GuesthouseEntity> findByLocation(String location);
}
