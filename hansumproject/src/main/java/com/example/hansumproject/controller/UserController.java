package com.example.hansumproject.controller;

import com.example.hansumproject.dto.*;
import com.example.hansumproject.entity.UserEntity;
import com.example.hansumproject.jwt.JWTUtil;
import com.example.hansumproject.service.GuesthouseService;
import com.example.hansumproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    // 유저 정보 조회
    @GetMapping("")
    public ResponseEntity<?> showUser(@RequestHeader("access") String accessToken) {

        // JWT 토큰에서 사용자 ID 추출
        Long userId = jwtUtil.getUserId(accessToken);

        // UserService를 통해 유저 정보 조회
        UserEntity user = userService.getUserInfo(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("name", user.getName());
        response.put("phone", user.getPhone());
        response.put("sex", user.getSex());
        response.put("birthday", user.getBirthday());
        response.put("nickname", user.getNickname());
        response.put("mbti", user.getMbti());
        response.put("userAgreement", user.getUserAgreement());

        response.put("interestedLocation", user.getListInterestedLocation());
        response.put("interestHobby", user.getListInterestedHobby());
        response.put("interestedFood", user.getListInterestedFood());

        response.put("message", "User Info retrieved successfully");

        return ResponseEntity.ok(response);
    }

    // 유저 닉네임 수정
    @PutMapping("/nickname")
    public ResponseEntity<?> updateNickname(@RequestHeader("access") String accessToken, @RequestBody UserDto userDto) {
        // JWT 토큰에서 사용자 ID 추출
        Long userId = jwtUtil.getUserId(accessToken);

        // UserService를 통해 유저 정보 조회 및 닉네임 업데이트
        UserEntity user = userService.updateNickname(userId, userDto.getNickname());

        // 성공 응답
        Map<String, Object> response = new HashMap<>();
        response.put("nickname", user.getNickname());
        response.put("message", "Nickname updated successfully");
        return ResponseEntity.ok(response);
    }

    // 찜 등록
    @PostMapping("/dibs")
    public ResponseEntity<Object> addDibs(@RequestHeader("access") String accessToken, @RequestBody Map<String, Object> request) {

        Long userId = jwtUtil.getUserId(accessToken);
        Long guesthouseId = Long.parseLong(request.get("guesthouseId").toString());

        // guesthouseId가 null인 경우
        if (guesthouseId == null) {
            throw new IllegalArgumentException("guesthouseId is required");
        }

        // Dibs 추가 로직 실행
        userService.addDibs(userId, guesthouseId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "guesthouse successfully added to dibs");
        return ResponseEntity.ok(response);
    }

    // 찜 삭제
    @DeleteMapping("/dibs")
    public ResponseEntity<Object> removeDibs(@RequestHeader("access") String accessToken, @RequestBody Map<String, Object> request) {
        Long userId = jwtUtil.getUserId(accessToken);
        Long guesthouseId = Long.parseLong(request.get("guesthouseId").toString());

        // guesthouseId가 null인 경우
        if (guesthouseId == null) {
            throw new IllegalArgumentException("guesthouseId is required");
        }

        userService.removeDibs(userId, guesthouseId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "guesthouse successfully removed from dibs");
        return ResponseEntity.ok(response);
    }

    // 찜 목록
    @GetMapping("/dibs")
    public ResponseEntity<?> getUserDibs(@RequestHeader("access") String accessToken) {
        Long userId = jwtUtil.getUserId(accessToken);
        Map<String, Object> dibsList = userService.getUserDibs(userId);
        return ResponseEntity.ok(dibsList);
    }

    // 관심사 수정
    @PutMapping("/interest")
    public ResponseEntity<Object> updateInterests(@RequestHeader("access") String accessToken, @RequestBody UserInterestDto userInterestDto) {
        Long userId = jwtUtil.getUserId(accessToken);

        log.info("Service before");

        // 관심사 업데이트 로직 실행
        userService.updateUserInterests(userId, userInterestDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Interests updated successfully");
        return ResponseEntity.ok(response);
    }

    // 리뷰 작성
    @PostMapping("/review")
    public ResponseEntity<?> createReview(HttpServletRequest request, @RequestBody Map<String, Object> requestBody) {

        // 필요 정보 가져오기
        String access = request.getHeader("access");
        Long userId = jwtUtil.getUserId(access);

        // guesthouseId와 rating 값 검증
        if (!requestBody.containsKey("guesthouseId")) {
            return ResponseEntity.badRequest().body(Map.of("errorMessage", "guesthouseId is required"));
        }
        if (!requestBody.containsKey("rating")) {
            return ResponseEntity.badRequest().body(Map.of("errorMessage", "rating is required"));
        }

        // 값 형식이 제대로 넘어왔는지 확인
        Long guesthouseId;
        Float rating;
        try {
            guesthouseId = Long.parseLong(requestBody.get("guesthouseId").toString());
            rating = Float.parseFloat(requestBody.get("rating").toString());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("errorMessage", "Invalid format for guesthouseId or rating"));
        }
        Map<String, Object> response = userService.createReview(userId, guesthouseId, rating);

        return ResponseEntity.ok(response);
    }

    // 사용자 예약 현황 조회
    @GetMapping("/reservation-record")
    public ResponseEntity<?> getUserReservations(HttpServletRequest request) {
        String access = request.getHeader("access");
        Long userId = jwtUtil.getUserId(access);

        ResponseEntity<?> reservationDtos = userService.getUserReservations(userId);

        // 404 상태 코드인 경우 그대로 반환
        if (reservationDtos.getStatusCode() == HttpStatus.NOT_FOUND) {
            return reservationDtos;
        }

        return ResponseEntity.ok(Map.of("reservation_records", reservationDtos.getBody()));
    }

    // 같이 사용한 게스트 조회
    @GetMapping("/guest/{reservationId}")
    public ResponseEntity<?> getGuestsByReservation(@PathVariable Long reservationId) throws Exception {
        return ResponseEntity.ok(guesthouseService.findGuestsByReservationId(reservationId));
    }

    // 스티커 작성
    @PostMapping("/sticker")
    public ResponseEntity<?> sendSticker(@RequestBody List<StickerDto> stickerDtos) {

        for (StickerDto stickerDto : stickerDtos) {
            // 필요 정보 가져오기
            Long recipientId = stickerDto.getUserId();
            List<String> stickerTexts = stickerDto.getStickerTexts();

            // 입력 값 검증
            if (recipientId == null || stickerTexts == null || stickerTexts.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("errorMessage", "Invalid request format"));
            }

            userService.sendStickers(recipientId, stickerTexts);
        }

        return ResponseEntity.ok(Map.of("message", "Stickers sent successfully"));
    }

    // 사용자가 받은 스티커 조회
    @GetMapping("/sticker")
    public ResponseEntity<?> getUserStickers(@RequestParam(required = false) Long userId, HttpServletRequest request) {
        // 사용자 ID 가져오기
        String access = request.getHeader("access");
        Long defaultUserId = jwtUtil.getUserId(access);
        // 요청 파라미터에서 userId가 제공되지 않았다면 JWT의 userId 사용
        // userId가 들어왔다는 것은 다른 사람의 스티커를 조회하겠다는 것.
        Long effectiveUserId = (userId != null) ? userId : defaultUserId;

        ResponseEntity<?> response = userService.getUserStickers(effectiveUserId);

        // 404 상태 코드인 경우 그대로 반환
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            return response;
        }

        // 응답 바디를 StickerDto 리스트로 캐스팅
        List<StickerDto> stickerDtos = (List<StickerDto>) response.getBody();

        // StickerDto 리스트를 새로운 형식으로 변환
        List<Map<?,?>> stickers = stickerDtos.stream()
                .map(stickerDto -> Map.of(
                        "stickerId", stickerDto.getStickerId(),
                        "stickerText", stickerDto.getStickerTexts().get(0),
                        "stickerCount", stickerDto.getStickerCount()
                )).collect(Collectors.toList());

        // 최종 응답 형식 구성
        Map<String, Object> responseBody = Map.of(
                "userId", effectiveUserId,
                "stickers", stickers
        );

        return ResponseEntity.ok(responseBody);
    }
}
