package com.example.hansumproject.repository;

import com.example.hansumproject.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    //Username으로 등록된 데이터가 있는지 확인
    Boolean existsByUsername(String username);
}
