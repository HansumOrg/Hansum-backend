package com.example.hansumproject.service;

import com.example.hansumproject.dto.ReservationDto;
import com.example.hansumproject.dto.StickerDto;
import com.example.hansumproject.entity.*;
import com.example.hansumproject.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GuesthouseRepository guesthouseRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private StickerRepository stickerRepository;

    @Autowired
    private DibsRepository dibsRepository;

    // 유저 정보 조회
    public UserEntity getUserInfo(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    // username 중복확인 메서드
    public boolean existsByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        return userRepository.existsByUsername(username);
    }

    // nickname 중복확인 메서드
    public boolean existsByNickname(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new IllegalArgumentException("Nickname cannot be null or empty");
        }
        return userRepository.existsByNickname(nickname);
    }

    // 유저 닉네임 수정
    @Transactional
    public UserEntity updateNickname(Long userId, String newNickname) {
        if (newNickname == null || newNickname.isEmpty()) {
            throw new IllegalArgumentException("Nickname cannot be null or empty");
        }

        // 수정할 닉네임이 중복인지
        if (existsByNickname(newNickname)) {
            throw new IllegalArgumentException("Nickname is already in use");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setNickname(newNickname);

        return userRepository.save(user);
    }

    // 찜 등록
    @Transactional
    public void addDibs(Long userId, Long guesthouseId) {

        // 중복되는 찜이 있을 경우
        if (dibsRepository.existsByUser_UserIdAndGuesthouse_GuesthouseId(userId, guesthouseId)) {
            throw new IllegalArgumentException("Dibs already exists for this user and guesthouse");
        }

        // 사용자가 존재하지 않을 경우
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 게스트하우스가 존재하지 않을 경우
        GuesthouseEntity guesthouse = guesthouseRepository.findById(guesthouseId)
                .orElseThrow(() -> new IllegalArgumentException("Guesthouse not found"));

        DibsEntity dibs = new DibsEntity();
        dibs.setUser(user);
        dibs.setGuesthouse(guesthouse);

        dibsRepository.save(dibs);
    }

    // 게스트하우스 리뷰 작성
    public Map<String, Object> createReview(Long userId, Long guesthouseId, Float rating) {
        // 평점 유효성 검증
        if (rating < 0 || rating > 5 || (rating * 10) % 5 != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating must be between 0 and 5 and in increments of 0.5");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        GuesthouseEntity guesthouse = guesthouseRepository.findById(guesthouseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid guesthouse ID: " + guesthouseId));

        ReviewEntity review = new ReviewEntity();
        review.setUser(user);
        review.setGuesthouse(guesthouse);
        review.setRating(rating);
        review.setCreatedDate(LocalDateTime.now());

        reviewRepository.save(review);

        // 평점 업데이트
        updateGuesthouseRating(guesthouse);

        return Map.of(
                "message", "Review created successfully"
        );
    }

    // 리뷰가 달릴 때 게스트하우스 평점 업데이트
    public void updateGuesthouseRating(GuesthouseEntity guesthouse) {
        List<ReviewEntity> reviews = reviewRepository.findByGuesthouse(guesthouse);
        double averageRating = reviews.stream()
                .mapToDouble(ReviewEntity::getRating)
                .average()
                .orElse(0.0);

        // 소숫점 첫째 자리까지 반올림
        float roundedRating = Math.round(averageRating * 10) / 10.0f;

        guesthouse.setRating(roundedRating);
        guesthouseRepository.save(guesthouse);
    }

    // 사용자의 예약 현황 조회
    public List<ReservationDto> getUserReservations(Long userId) {
        List<ReservationEntity> reservationEntities = reservationRepository.findByUserUserId(userId);
        return reservationEntities.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // ReservationEntity -> ReservationDto
    private ReservationDto convertToDto(ReservationEntity reservationEntity) {
        return new ReservationDto(
                reservationEntity.getReservationId(),
                reservationEntity.getUser().getUserId(),
                reservationEntity.getGuesthouse().getGuesthouseId(),
                reservationEntity.getCheckinDate().toString(),
                reservationEntity.getCheckoutDate().toString()
        );
    }

    // 스티커 작성
    @Transactional
    public void sendStickers(Long recipientId, List<String> stickerTexts) {
        UserEntity recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipient not found"));

        for (String text : stickerTexts) {
            StickerEntity sticker = stickerRepository.findByUserAndStickerText(recipient, text)
                    .orElse(new StickerEntity(recipient, text, 0));

            sticker.setStickerCount(sticker.getStickerCount() + 1);

            stickerRepository.save(sticker);
        }
    }

    // 받은 스티커 조회
    public List<StickerDto> getUserStickers(Long userId) {
        List<StickerEntity> stickerEntities = stickerRepository.findByUserUserId(userId);
        if (stickerEntities.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found or no stickers available");
        }

        return stickerEntities.stream()
                .map(sticker -> new StickerDto(
                        sticker.getStickerId(),
                        sticker.getUser().getUserId(),
                        List.of(sticker.getStickerText()),
                        sticker.getStickerCount()
                )).collect(Collectors.toList());
    }

}
