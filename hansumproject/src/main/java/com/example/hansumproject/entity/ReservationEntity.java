package com.example.hansumproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservation_id; // 예약 ID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // 회원 ID

    @ManyToOne
    @JoinColumn(name = "guesthouse_id", nullable = false)
    private GuesthouseEntity guesthouse; // 게스트하우스 ID

    @Column(nullable = false)
    private Timestamp checkin_date; // 체크인 날짜

    @Column(nullable = false)
    private Timestamp checkout_date; // 체크아웃 날짜
}
