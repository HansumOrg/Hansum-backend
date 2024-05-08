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
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long review_id; // 리뷰 ID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // 회원 ID

    @ManyToOne
    @JoinColumn(name = "guesthouse_id", nullable = false)
    private GuesthouseEntity guesthouse; // 게스트하우스 ID

    @Column(nullable = false)
    private float rating; // 점수

    @Column(nullable = false)
    private Timestamp createdDate; // 작성 일자
}
