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
    @Column(name = "reservationId")
    private Long reservationId; // 예약 ID

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user; // 회원 ID

    @ManyToOne
    @JoinColumn(name = "guesthouseId", nullable = false)
    private GuesthouseEntity guesthouse; // 게스트하우스 ID

    @Column(name = "checkinDate", nullable = false)
    private Timestamp checkinDate; // 체크인 날짜

    @Column(name = "checkoutDate", nullable = false)
    private Timestamp checkoutDate; // 체크아웃 날짜
}
