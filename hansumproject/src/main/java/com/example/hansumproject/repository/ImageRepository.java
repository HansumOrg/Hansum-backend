package com.example.hansumproject.repository;

import com.example.hansumproject.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    ImageEntity findByNameContaining(String fileName);
}
