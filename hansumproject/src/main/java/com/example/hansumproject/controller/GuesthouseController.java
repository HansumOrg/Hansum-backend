package com.example.hansumproject.controller;

import com.example.hansumproject.repository.GuesthouseRepository;
import com.example.hansumproject.service.GuesthouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GuesthouseController {

    @Autowired
    private GuesthouseService guesthouseService;

    // 게스트하우스 상세 정보 조회
    @GetMapping("/guesthouses/{guesthouseId}")
    public ResponseEntity<?> getGuesthouseDetail(@PathVariable Long guesthouseId) {
        return guesthouseService.getGuesthouseDetail(guesthouseId);
    }


}
