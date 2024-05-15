package com.example.hansumproject.controller;

import com.example.hansumproject.dto.ReservationDto;
import com.example.hansumproject.dto.StickerDto;
import com.example.hansumproject.jwt.JWTUtil;
import com.example.hansumproject.service.GuesthouseService;
import com.example.hansumproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GuesthouseService guesthouseService;

    @Autowired
    private JWTUtil jwtUtil;

    // 리뷰 작성
    @PostMapping("/review")
    public ResponseEntity<?> createReview(HttpServletRequest request, @RequestBody Map<String, Object> requestBody) {

        // 필요 정보 가져오기
        String access = request.getHeader("access");
        Long userId = jwtUtil.getUserId(access);
        Long guesthouseId = Long.parseLong(requestBody.get("guesthouse_id").toString());
        Float rating = Float.parseFloat(requestBody.get("rating").toString());

        Map<String, Object> response = userService.createReview(userId, guesthouseId, rating);

        return ResponseEntity.ok(response);
    }

    // 사용자 예약 현황 조회
    @GetMapping("/reservation-record")
    public ResponseEntity<?> getUserReservations(HttpServletRequest request) {
        String access = request.getHeader("access");
        Long userId = jwtUtil.getUserId(access);

        List<ReservationDto> reservationDtos = userService.getUserReservations(userId);

        return ResponseEntity.ok(Map.of("reservation_records", reservationDtos));
    }

    // 같이 사용한 게스트 조회
    @GetMapping("/guest/{reservationId}")
    public ResponseEntity<?> getGuestsByReservation(@PathVariable Long reservationId) throws Exception {
        return ResponseEntity.ok(guesthouseService.findGuestsByReservationId(reservationId));
    }

    // 스티커 작성
    @PostMapping("/sticker")
    public ResponseEntity<?> sendSticker(@RequestBody StickerDto stickerDto) {

        // 필요 정보 가져오기
        Long recipientId = stickerDto.getUserId();
        List<String> stickerTexts = stickerDto.getStickerTexts();

        userService.sendStickers(recipientId, stickerTexts);
        return ResponseEntity.ok(Map.of("message", "Sticker sent successfully"));
    }
}
