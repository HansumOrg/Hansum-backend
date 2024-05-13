package com.example.hansumproject.service;

import com.example.hansumproject.entity.GuesthouseEntity;
import com.example.hansumproject.repository.GuesthouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class GuesthouseService {

    @Autowired
    private GuesthouseRepository guesthouseRepository;

    // 게스트하우스 상세 정보 조회
    public ResponseEntity<?> getGuesthouseDetail(Long guesthouseId) {
        Optional<GuesthouseEntity> target = guesthouseRepository.findById(guesthouseId);
        if (target.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(target.get());
    }
}
