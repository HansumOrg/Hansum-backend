package com.example.hansumproject.repository;

import com.example.hansumproject.entity.GuesthouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuesthouseRepository extends JpaRepository<GuesthouseEntity, Long> {

    GuesthouseEntity findByGuesthouseNameContaining(String fileName);
}
