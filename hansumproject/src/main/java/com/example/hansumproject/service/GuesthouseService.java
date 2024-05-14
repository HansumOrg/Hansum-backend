package com.example.hansumproject.service;

import com.example.hansumproject.dto.ReservationDto;
import com.example.hansumproject.entity.GuesthouseEntity;
import com.example.hansumproject.entity.MbtiProbEntity;
import com.example.hansumproject.entity.ReservationEntity;
import com.example.hansumproject.entity.UserEntity;
import com.example.hansumproject.repository.GuesthouseRepository;
import com.example.hansumproject.repository.MbtiProbRepository;
import com.example.hansumproject.repository.ReservationRepository;
import com.example.hansumproject.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GuesthouseService {

    @Autowired
    private GuesthouseRepository guesthouseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MbtiProbRepository mbtiProbRepository;

    // 게스트하우스 상세 정보 조회
    public ResponseEntity<?> getGuesthouseDetail(Long guesthouseId) {
        Optional<GuesthouseEntity> target = guesthouseRepository.findById(guesthouseId);
        if (target.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Guesthouse not found with ID: " + guesthouseId);
        }
        return ResponseEntity.ok(target.get());
    }

    // 게스트하우스 예약(userId 관련 구현 필요)
    @Transactional
    public ResponseEntity<?> createReservation(Long guesthouseId, ReservationDto reservationDto) {
        Optional<GuesthouseEntity> guesthouseEntity = guesthouseRepository.findById(guesthouseId);
        Optional<UserEntity> userEntity = userRepository.findById(reservationDto.getUser().getUserId());

        if (guesthouseEntity.isEmpty() || userEntity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The requested user or guesthouse ID does not exist.");
        }

        ReservationEntity reservation = new ReservationEntity();
        reservation.setUser(userEntity.get());
        reservation.setGuesthouse(guesthouseEntity.get());
        reservation.setCheckinDate(Timestamp.valueOf(reservationDto.getCheckinDate().toLocalDateTime()));
        reservation.setCheckoutDate(Timestamp.valueOf(reservationDto.getCheckoutDate().toLocalDateTime()));

        reservationRepository.save(reservation);

        return ResponseEntity.ok(Map.of(
                "message", "Reservation created successfully"
        ));
    }

    // 추천 게스트하우스 조회
    public List<Map<String, ?>> getRecommendationsByMbti(String mbti) {
        List<MbtiProbEntity> recommendations = mbtiProbRepository.findByMbtiOrderByProbabilityDesc(mbti);

        if (recommendations.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "NO guesthouse recommendations");
        }
        return recommendations.stream().limit(10).map(r -> Map.of (
                "guesthouse_name", r.getGuesthouse().getGuesthouseName(),
                "guesthouse_id", r.getGuesthouse().getGuesthouseId(),
                "imageUrl", r.getGuesthouse().getImageUrl(),
                "probability", r.getProbability(),
                "rank", recommendations.indexOf(r) + 1
        )).collect(Collectors.toList());
    }

}
