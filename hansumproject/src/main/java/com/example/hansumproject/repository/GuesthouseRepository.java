package com.example.hansumproject.repository;

import com.example.hansumproject.entity.GuesthouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GuesthouseRepository extends JpaRepository<GuesthouseEntity, Long> {
    GuesthouseEntity findByImageUrlContaining(String fileName);

    @Query("SELECT g FROM GuesthouseEntity g WHERE " +
            "(:guesthouseName IS NOT NULL OR :location IS NOT NULL) " +
            "AND (:guesthouseName IS NULL OR g.guesthouseName LIKE %:guesthouseName%) " +
            "AND (:location IS NULL OR g.location LIKE %:location%) " +
            "AND (:mood IS NULL OR g.mood LIKE %:mood%) " +
            "AND g.price BETWEEN :minPrice AND :maxPrice " +
            "AND (:facilities IS NULL OR EXISTS (SELECT f FROM FacilityEntity f WHERE f.guesthouse = g AND " +
            "(CASE WHEN 'party' IN :facilities THEN f.party = 1 ELSE true END) " +
            "AND (CASE WHEN 'breakfast' IN :facilities THEN f.breakfast = 1 ELSE true END) " +
            "AND (CASE WHEN 'singleroom' IN :facilities THEN f.singleroom = 1 ELSE true END) " +
            "AND (CASE WHEN 'parking' IN :facilities THEN f.parking = 1 ELSE true END) " +
            "AND (CASE WHEN 'swimmingpool' IN :facilities THEN f.swimmingpool = 1 ELSE true END) " +
            "AND (CASE WHEN 'womanOnly' IN :facilities THEN f.womanOnly = 1 ELSE true END)))")
    List<GuesthouseEntity> searchGuesthouses(
            @Param("guesthouseName") String guesthouseName,
            @Param("location") String location,
            @Param("mood") String mood,
            @Param("facilities") List<String> facilities,
            @Param("minPrice") int minPrice,
            @Param("maxPrice") int maxPrice);
}
