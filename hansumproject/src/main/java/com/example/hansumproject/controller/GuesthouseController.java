package com.example.hansumproject.controller;

import com.example.hansumproject.dto.ReservationDto;
import com.example.hansumproject.entity.ReservationEntity;
import com.example.hansumproject.entity.ReviewEntity;
import com.example.hansumproject.repository.GuesthouseRepository;
import com.example.hansumproject.service.GuesthouseService;
//import com.example.hansumproject.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/guesthouses")
public class GuesthouseController {

    @Autowired
    private GuesthouseService guesthouseService;

    // 게스트하우스 상세 정보 조회
    @GetMapping("/{guesthouseId}")
    public ResponseEntity<?> getGuesthouseDetail(@PathVariable Long guesthouseId) {
        return guesthouseService.getGuesthouseDetail(guesthouseId);
    }

    // 게스트하우스 이미지 조회
    @GetMapping("/{guesthouseId}/{imageUrl}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable("imageUrl") String fileName) throws IOException {
        byte[] downloadImage = guesthouseService.downloadImageFromFileSystem(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/jpeg"))
                .body(downloadImage);
    }

    // 추천 게스트하우스 조회
    @GetMapping("/recommendation")
    public ResponseEntity<?> getRecommendations(@RequestParam String mbti) {
        // 서비스 호출
        List<Map<String, ?>> recommendations = guesthouseService.getRecommendationsByMbti(mbti);

        return ResponseEntity.ok(Map.of(
                "recommendations", recommendations
        ));
    }

    // 게스트하우스 리뷰 조회
    @GetMapping("/{guesthouseId}/reviews")
    public ResponseEntity<?> getGuesthouseReviews(@PathVariable("guesthouseId") Long guesthouseId) {
        Map<String, Object> reviewData = guesthouseService.getFormattedReviewByGuesthouseId(guesthouseId);
        return ResponseEntity.ok(reviewData);
    }

}
