package com.example.hansumproject.repository;


import com.example.hansumproject.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByGuesthouseGuesthouseId(Long guesthouseId);

    List<ReservationEntity> findByUserUserId(Long userId);

    @Query("SELECT r FROM ReservationEntity r WHERE r.guesthouse.guesthouseId = :guesthouseId " +
            "AND NOT (r.checkoutDate <= :checkin OR r.checkinDate >= :checkout)")
    List<ReservationEntity> findOverlappingReservations(@Param("guesthouseId") Long guesthouseId,
                                                        @Param("checkin") Date checkinDate,
                                                        @Param("checkout") Date checkoutDate);
}
