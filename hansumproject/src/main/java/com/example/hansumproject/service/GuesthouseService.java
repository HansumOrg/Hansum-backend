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
import java.io.FileNotFoundException;
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

    @Autowired
    private DibsRepository dibsRepository;

    @Autowired
    private FacilityRepository facilityRepository;

    // 이미지 파일을 Base64로 인코딩하여 문자열로 반환
    public String encodeFileToBase64Binary(String fileName) {
        try {
            // 해당 이미지 path 가져오기
            GuesthouseEntity guesthouseEntity = guesthouseRepository.findByImageUrlContaining(fileName);
            String imageUrl = guesthouseEntity.getImageUrl();

            // 기본 이미지 경로 설정
            String imageDir = "/app/images";
            String fullPath = Paths.get(imageDir, imageUrl).toString();

            // 파일 존재 여부 확인
            File file = new File(fullPath);
            if (!file.exists()) {
                throw new FileNotFoundException("File not found: " + fullPath);
            }

            // byte 배열로 읽어오기
            byte[] fileContent = Files.readAllBytes(file.toPath());

            // Base64로 인코딩
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (FileNotFoundException e) {
            // 파일을 찾을 수 없는 경우
            throw new IllegalArgumentException("File not found exception: " + e.getMessage(), e);
        } catch (IOException e) {
            // 입출력 오류 발생
            throw new IllegalArgumentException("IO exception occurred while reading the file: " + e.getMessage(), e);
        } catch (Exception e) {
            // 다른 모든 예외에 대한 처리
            throw new IllegalArgumentException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }

    // 게스트하우스 상세 정보 조회
    public ResponseEntity<?> getGuesthouseDetail(Long guesthouseId) {
        Optional<GuesthouseEntity> target = guesthouseRepository.findById(guesthouseId);
        if (target.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Guesthouse not found with ID: " + guesthouseId);
        }

        // 반환 Entity 만들기
        GuesthouseEntity guesthouseEntity = target.get();
        Map<String, Object> response = new HashMap<>();

        response.put("guesthouseId", guesthouseEntity.getGuesthouseId());
        response.put("guesthouseName", guesthouseEntity.getGuesthouseName());
        response.put("address", guesthouseEntity.getAddress());
        response.put("location", guesthouseEntity.getLocation());
        response.put("price", guesthouseEntity.getPrice());
        response.put("phone", guesthouseEntity.getPhone());
        response.put("rating", guesthouseEntity.getRating());
        response.put("mood", guesthouseEntity.getMood());

        // 이미지 Base64 변화
        String base64Image = encodeFileToBase64Binary(guesthouseEntity.getImageUrl());
        response.put("imageBase64", "data:image/jpeg;base64," + base64Image);

        return ResponseEntity.ok(response);
    }

    // FileSystem에서 이미지를 다운로드하여 base64 encoding 하고 결과를 다시 decode해서 반환해보기(테스트용)
    public byte[] downloadImageFromFileSystem(String fileName) throws IOException {
        log.info("filename:{}", fileName);
        // fileName가지고 게스트하우스 Entity 가져오기
        GuesthouseEntity guesthouseEntity = guesthouseRepository.findByImageUrlContaining(fileName);
        if (guesthouseEntity == null) {
            log.error("No guesthouse entity found for fileName: {}", fileName);
            throw new FileNotFoundException("Guesthouse entity not found for the given fileName");
        }

        // ImageUrl 가져오기
        String test = guesthouseEntity.getImageUrl();
        log.info("test : {}", test);
        // 저장된 이미지 읽어오기(상대경로)
        String currentWorkingDir = System.getProperty("user.dir");
        String fullPath = Paths.get(currentWorkingDir,"/src/main/java/com/example/hansumproject/files",test).toString();

        log.info("download filePath : {}",fullPath);

        File file = new File(fullPath);
        if (!file.exists()) {
            log.error("File does not exist at path: {}", fullPath);
            throw new FileNotFoundException("File does not exist at path: " + fullPath);
        }

        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            log.info("File content length : {}", fileContent.length);
            // base64 encoding
            String encodedString = Base64.getEncoder().encodeToString(fileContent);
            log.info("encodedString length : {}", encodedString.length());
            // base64 decoding
            byte[] decodedImgae = Base64.getDecoder().decode(encodedString);

            return decodedImgae;
        } catch (IOException e) {
            log.error("Error reading file at path: {}", fullPath, e);
            throw e;
        }
    }

    // 게스트하우스 검색 결과 조회
    public Map<String, Object> searchGuesthouses(String guesthouseName, String location, String mood, List<String> facilities, int minPrice, int maxPrice, Long userId) {
        try {
            List<GuesthouseEntity> guesthouses = guesthouseRepository.searchGuesthouses(guesthouseName, location, mood, facilities, minPrice, maxPrice);

            // 유저의 찜 목록 가져오기
            List<DibsEntity> dibsList = dibsRepository.findByUserUserId(userId);
            Set<Long> dibsGuesthouseIds = dibsList.stream()
                    .map(dibs -> dibs.getGuesthouse().getGuesthouseId())
                    .collect(Collectors.toSet());

            List<Map<String, Object>> guesthouseList = guesthouses.stream().map(guesthouse -> {
                Map<String, Object> guesthouseMap = new HashMap<>();
                guesthouseMap.put("guesthouseId", guesthouse.getGuesthouseId());
                guesthouseMap.put("guesthouseName", guesthouse.getGuesthouseName());
                guesthouseMap.put("address", guesthouse.getAddress());
                guesthouseMap.put("location", guesthouse.getLocation());
                guesthouseMap.put("price", guesthouse.getPrice());
                guesthouseMap.put("phone", guesthouse.getPhone());
                guesthouseMap.put("rating", guesthouse.getRating());
                String base64Image = encodeFileToBase64Binary(guesthouse.getImageUrl()); // 이미지 인코딩
                guesthouseMap.put("imageBase64", "data:image/jpeg;base64," + base64Image);
                guesthouseMap.put("mood", guesthouse.getMood());
                guesthouseMap.put("dibs", dibsGuesthouseIds.contains(guesthouse.getGuesthouseId())); // 찜 여부
                return guesthouseMap;
            }).collect(Collectors.toList());

            Map<String, Object> result = new HashMap<>();
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


        return recommendations.stream().limit(10).map(r -> {
            Map<String, Object> recommendationMap = new HashMap<>();
            recommendationMap.put("guesthouseName", r.getGuesthouse().getGuesthouseName());
            recommendationMap.put("guesthouseId", r.getGuesthouse().getGuesthouseId());
            recommendationMap.put("probability", r.getProbability());
            recommendationMap.put("rank", recommendations.indexOf(r) + 1);

            // 이미지 encoding
            String base64Image = encodeFileToBase64Binary(r.getGuesthouse().getImageUrl());
            recommendationMap.put("imageBase64", "data:image/jpeg;base64," + base64Image);
            return recommendationMap;
        }).collect(Collectors.toList());
    }

    // 게스트하우스 리뷰 조회
    public Map<String, Object> getFormattedReviewByGuesthouseId(Long guesthouseId) {
        List<ReviewEntity> reviews = reviewRepository.findByGuesthouse_GuesthouseId(guesthouseId);
        if (reviews.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Guesthouse NOT FOUND");
        }

        List<Map<String, ?>> formattedReviews = reviews.stream().map(review -> Map.of(
                "username", review.getUser().getName(),
                "guesthouseName", review.getGuesthouse().getGuesthouseName(),
                "rating", review.getRating(),
                "createdDate", review.getCreatedDate()
        )).collect(Collectors.toList());

        return Map.of(
                "guesthouseName", reviews.get(0).getGuesthouse().getGuesthouseName(),
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

    // 예약한 사람들의 MBTI 조회
    public Map<String, Object> getMembersByGuesthouse(Long guesthouseId) {
        GuesthouseEntity guesthouseEntity = guesthouseRepository.findById(guesthouseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guesthouse not found"));

        List<ReservationEntity> reservations = reservationRepository.findByGuesthouseGuesthouseId(guesthouseId);
        if (reservations.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No reservations found");
        }

        List<Map<String, ?>> members = reservations.stream()
                .map(reservation -> Map.of(
                        "userId", reservation.getUser().getUserId(),
                        "username", reservation.getUser().getUsername(),
                        "nickname", reservation.getUser().getNickname(),
                        "mbti", reservation.getUser().getMbti()
                )).collect(Collectors.toList());

        return Map.of(
                "guesthouseId", guesthouseId,
                "guesthouseName", guesthouseEntity.getGuesthouseName(),
                "members", members
        );
    }

    // 같이 사용한 게스트 조회
    public Map<String, Object> findGuestsByReservationId(Long reservationId) throws Exception {
        ReservationEntity reservationEntity = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        List<ReservationEntity> reservationEntities = reservationRepository.findOverlappingReservations(
                reservationEntity.getGuesthouse().getGuesthouseId(),
                reservationEntity.getCheckinDate(),
                reservationEntity.getCheckoutDate());

        List<Map<String, ?>> guests = reservationEntities.stream()
                .filter(r -> !r.getReservationId().equals(reservationId))
                .map(r -> Map.of(
                        "userId", r.getUser().getUserId(),
                        "username", r.getUser().getUsername(),
                        "nickname", r.getUser().getNickname(),
                        "mbti", r.getUser().getMbti()
                )).collect(Collectors.toList());

        return Map.of(
                "guesthouseId", reservationEntity.getGuesthouse().getGuesthouseId(),
                "guesthouseName", reservationEntity.getGuesthouse().getGuesthouseName(),
                "checkinDate", reservationEntity.getCheckinDate().toString(),
                "checkoutDate", reservationEntity.getCheckoutDate().toString(),
                "guests", guests
        );

    }
}

