package com.example.hansumproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reviewId")
    private Long review_id; // 리뷰 ID

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user; // 회원 ID

    @ManyToOne
    @JoinColumn(name = "guesthouseId", nullable = false)
    private GuesthouseEntity guesthouse; // 게스트하우스 ID

    @Column(name = "rating", nullable = false)
    private Float rating; // 점수

    @Column(name = "createdDate", nullable = false)
    private LocalDateTime createdDate; // 작성 일자
}
