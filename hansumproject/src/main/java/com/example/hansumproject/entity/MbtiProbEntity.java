package com.example.hansumproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MbtiProbEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long probability_id; // 확률 ID

    @ManyToOne
    @JoinColumn(name = "guesthouse_id", nullable = false)
    private GuesthouseEntity guesthouse; // 게스트하우스 ID

    @Column(nullable = false)
    private String mbti; // MBTI

    @Column(nullable = false)
    private float probability; // 확률
}
