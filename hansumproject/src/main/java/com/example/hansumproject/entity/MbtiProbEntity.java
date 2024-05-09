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
    @Column(name = "probabilityId")
    private Long probabilityId; // 확률 ID

    @ManyToOne
    @JoinColumn(name = "guesthouseId", nullable = false)
    private GuesthouseEntity guesthouse; // 게스트하우스 ID

    @Column(name = "mbti", nullable = false)
    private String mbti; // MBTI

    @Column(name = "probability", nullable = false)
    private float probability; // 확률
}
