package com.example.hansumproject.dto;

import com.example.hansumproject.entity.GuesthouseEntity;
import com.example.hansumproject.entity.UserEntity;
import java.sql.Timestamp;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ReservationDto {
    private Long reservationId; // 예약 ID
    private Long userId; // 회원 ID
    private Long guesthouseId; // 게스트하우스 ID
    private String checkinDate; // 체크인 날짜(string)
    private String checkoutDate; // 체크아웃 날짜(string)
}
