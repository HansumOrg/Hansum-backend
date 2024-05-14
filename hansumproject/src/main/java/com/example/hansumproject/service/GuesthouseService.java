package com.example.hansumproject.service;

import com.example.hansumproject.entity.*;
import com.example.hansumproject.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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

    @Autowired
    private ReviewRepository reviewRepository;

    // 게스트하우스 상세 정보 조회
    public ResponseEntity<?> getGuesthouseDetail(Long guesthouseId) {
        Optional<GuesthouseEntity> target = guesthouseRepository.findById(guesthouseId);
        if (target.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Guesthouse not found with ID: " + guesthouseId);
        }
        return ResponseEntity.ok(target.get());
    }

    // FileSystem에서 이미지를 다운로드하여 byte 배열로 반환
    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        log.info("fileName : {}", fileName);
        // GuesthouseEntity에서 url명 가져와서 image 가져와도 될듯.
        GuesthouseEntity guesthouseEntity = guesthouseRepository.findByImageUrlContaining(fileName);
        String test = guesthouseEntity.getImageUrl();
        log.info("test : {}", test);
        String currentWorkingDir = System.getProperty("user.dir");
        String fullPath = Paths.get(currentWorkingDir,"/src/main/java/com/example/hansumproject/files",test).toString();

        log.info("download filePath : {}",fullPath);

        // 파일의 byte 배열을 읽어 반환
        return Files.readAllBytes(new File(fullPath).toPath());
    }

    // 게스트하우스 검색 결과 조회
    public Map<String, Object> searchGuesthouses(String location, String checkinDate, String checkoutDate) {
        try {
            List<GuesthouseEntity> guesthouses = guesthouseRepository.findByLocation(location);

            List<Map<String, Object>> guesthouseList = guesthouses.stream().map(guesthouse -> {
                Map<String, Object> guesthouseMap = new HashMap<>();
                guesthouseMap.put("guesthouse_id", guesthouse.getGuesthouseId());
                guesthouseMap.put("guesthouse_name", guesthouse.getGuesthouseName());
                guesthouseMap.put("address", guesthouse.getAddress());
                guesthouseMap.put("location", guesthouse.getLocation());
                guesthouseMap.put("price", guesthouse.getPrice());
                guesthouseMap.put("phone", guesthouse.getPhone());
                guesthouseMap.put("rating", guesthouse.getRating());
                guesthouseMap.put("imageUrl", guesthouse.getImageUrl() != null ? guesthouse.getImageUrl() : "default.jpg");
                guesthouseMap.put("mood", guesthouse.getMood());
                return guesthouseMap;
            }).collect(Collectors.toList());

            Map<String, Object> result = new HashMap<>();
            result.put("location", location);
            result.put("checkin_date", checkinDate);
            result.put("checkout_date", checkoutDate);
            result.put("guesthouses", guesthouseList);
            return result;
        } catch (Exception e) {
            throw e;
        }

    }

    // 추천 게스트하우스 조회
    public List<Map<String,?>> getRecommendationsByMbti(String mbti) {
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

    // 게스트하우스 리뷰 조회
    public Map<String, Object> getFormattedReviewByGuesthouseId(Long guesthouseId) {
        List<ReviewEntity> reviews = reviewRepository.findByGuesthouse_GuesthouseId(guesthouseId);
        if (reviews.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Guesthouse NOT FOUND");
        }

        List<Map<String, ?>> formattedReviews = reviews.stream().map(review -> Map.of(
                "user_name", review.getUser().getName(),
                "guesthouse_name", review.getGuesthouse().getGuesthouseName(),
                "rating", review.getRating(),
                "created_date", review.getCreatedDate()
        )).collect(Collectors.toList());

        return Map.of(
                "guesthouse_name", reviews.get(0).getGuesthouse().getGuesthouseName(),
                "reviews", formattedReviews
        );
    }

    // 게스트하우스 예약
    @Transactional
    public ReservationEntity createReservation(Long guesthouseId, Long userId, String checkinStr, String checkoutStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp checkinDate = new Timestamp(dateFormat.parse(checkinStr).getTime());
        Timestamp checkoutDate = new Timestamp(dateFormat.parse(checkoutStr).getTime());

        // 유저 Entity와 게스트하우스 Entity 찾기
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));
        GuesthouseEntity guesthouse = guesthouseRepository.findById(guesthouseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid guesthouse ID: " + guesthouseId));

        // 저장 Entity 생성
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setUser(user);
        reservationEntity.setGuesthouse(guesthouse);
        reservationEntity.setCheckinDate(checkinDate);
        reservationEntity.setCheckoutDate(checkoutDate);

        // 저장
        return reservationRepository.save(reservationEntity);

    }

}

