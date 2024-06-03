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
import java.util.Arrays;
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

    // 유효 MBTI list
    private static final List<String> VALID_MBTI_TYPES = Arrays.asList(
            "INTJ", "INTP", "ENTJ", "ENTP",
            "INFJ", "INFP", "ENFJ", "ENFP",
            "ISTJ", "ISFJ", "ESTJ", "ESFJ",
            "ISTP", "ISFP", "ESTP", "ESFP"
    );

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
    public ResponseEntity<?> searchGuesthouses(@RequestParam(value = "guesthouse_name", required = false) String guesthouse_name,
                                               @RequestParam(value = "location", required = false) String location,
                                               @RequestParam(value = "checkin_date", required = false) String checkin_date,
                                               @RequestParam(value = "checkout_date", required = false) String checkout_date,
                                               @RequestParam(value = "mood", required = false) String mood,
                                               @RequestParam(value = "facility", required = false) List<String> facilities,
                                               @RequestParam(value = "min_price", required = false, defaultValue = "0") int minPrice,
                                               @RequestParam(value = "max_price", required = false, defaultValue = "100000") int maxPrice,
                                               @RequestHeader("access") String accessToken){
        // 'access' 헤더에서 토큰 가져오기
        Long userId = jwtUtil.getUserId(accessToken);

        // 이름이나 위치 둘 다 제공되지 않으면 예외 처리
        if (guesthouse_name == null && location == null) {
            throw new IllegalArgumentException("guesthouse_name or location must be provided");
        }

        Map<String, Object> result = guesthouseService.searchGuesthouses(guesthouse_name, location, mood, facilities, minPrice, maxPrice, userId);
        return ResponseEntity.ok(result);
    }


    // 추천 게스트하우스 조회
    @GetMapping("/recommendation")
    public ResponseEntity<?> getRecommendations(@RequestParam String mbti) {
        // MBTI 값 검증
        if (mbti == null || !VALID_MBTI_TYPES.contains(mbti.toUpperCase())) {
            return ResponseEntity.badRequest().body(Map.of("errorMessage", "Invalid mbti"));
        }

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
                                               @RequestHeader("access") String accessToken,
                                               @RequestBody Map<String, String> requestBody) throws ParseException {

        // 'access' 헤더에서 토큰 가져오기
        Long userId = jwtUtil.getUserId(accessToken);

        // Body 값 가져오기
        String checkin = requestBody.get("checkinDate");
        String checkout = requestBody.get("checkoutDate");

        // 입력 값 검증
        if (checkin == null || checkin.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("errorMessage", "checkin date is required"));
        }
        if (checkout == null || checkout.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("errorMessage", "checkout date is required"));
        }

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

