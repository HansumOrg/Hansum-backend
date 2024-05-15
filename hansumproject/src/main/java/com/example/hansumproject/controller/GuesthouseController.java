package com.example.hansumproject.controller;

import com.example.hansumproject.dto.ReservationDto;
import com.example.hansumproject.entity.ReservationEntity;
import com.example.hansumproject.entity.ReviewEntity;
import com.example.hansumproject.jwt.JWTUtil;
import com.example.hansumproject.repository.GuesthouseRepository;
import com.example.hansumproject.service.GuesthouseService;
//import com.example.hansumproject.service.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/guesthouses")
public class GuesthouseController {

    @Autowired
    private GuesthouseService guesthouseService;

    @Autowired
    private JWTUtil jwtUtil;

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

    // 게스트하우스 검색 결과 조회
    @GetMapping("/search")
    public ResponseEntity<?> searchGuesthouses(@RequestParam(required = false) String location,
                                               @RequestParam(required = false) String checkin_date,
                                               @RequestParam(required = false) String checkout_date) {
        log.info("Received search request with location: {}, checkin_date: {}, checkout_date: {}", location, checkin_date, checkout_date);

        Map<String, Object> result = guesthouseService.searchGuesthouses(location, checkin_date, checkout_date);
        log.info("controller:{}",result.toString());
        return ResponseEntity.ok(result);
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

    // 게스트하우스 예약
    @PostMapping("/{guesthouseId}")
    public ResponseEntity<?> createReservation(@PathVariable Long guesthouseId,
                                               HttpServletRequest request,
                                               @RequestBody Map<String, String> requestBody) throws ParseException {
        String access = request.getHeader("access"); // 'access' 헤더에서 토큰 가져오기
        Long userId = jwtUtil.getUserId(access);

        // Body 값 가져오기
        String checkin = requestBody.get("checkin_date");
        String checkout = requestBody.get("checkout_date");

        // 예약 로직
        guesthouseService.createReservation(guesthouseId, userId, checkin, checkout);

        return ResponseEntity.ok(Map.of("message", "Reservation created successfully"));
    }

    // 게스트하우스 예약자들 MBTI 조회
    @GetMapping("/{guesthouseId}/members")
    public ResponseEntity<?> getMembers(@PathVariable Long guesthouseId) {
        Map<String, Object> memberInfo = guesthouseService.getMembersByGuesthouse(guesthouseId);
        return ResponseEntity.ok(memberInfo);
    }
}

