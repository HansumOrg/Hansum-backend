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
public class ReservationEntity {
    private Long reservation_id; // 예약 ID

    private UserEntity user; // 회원 ID

    private GuesthouseEntity guesthouse; // 게스트하우스 ID

    private Timestamp checkin_date; // 체크인 날짜

    private Timestamp checkout_date; // 체크아웃 날짜
}
