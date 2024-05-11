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

    private UserEntity user; // 회원 ID

    private GuesthouseEntity guesthouse; // 게스트하우스 ID

    private Timestamp checkinDate; // 체크인 날짜

    private Timestamp checkoutDate; // 체크아웃 날짜
}
