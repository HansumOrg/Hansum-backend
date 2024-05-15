package com.example.hansumproject.service;

import com.example.hansumproject.dto.ReservationDto;
import com.example.hansumproject.entity.GuesthouseEntity;
import com.example.hansumproject.entity.ReservationEntity;
import com.example.hansumproject.entity.ReviewEntity;
import com.example.hansumproject.entity.UserEntity;
import com.example.hansumproject.repository.GuesthouseRepository;
import com.example.hansumproject.repository.ReservationRepository;
import com.example.hansumproject.repository.ReviewRepository;
import com.example.hansumproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GuesthouseRepository guesthouseRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReservationRepository reservationRepository;

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
}
