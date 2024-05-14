package com.example.hansumproject.repository;

import com.example.hansumproject.entity.MbtiProbEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MbtiProbRepository extends JpaRepository<MbtiProbEntity, Long> {
    List<MbtiProbEntity> findByMbtiOrderByProbabilityDesc(String mbti);
}
