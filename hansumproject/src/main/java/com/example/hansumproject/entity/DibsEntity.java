package com.example.hansumproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DibsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dibsId")
    private Long dibsId; // 찜 ID

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user; // 회원 ID

    @ManyToOne
    @JoinColumn(name = "guesthouseId", nullable = false)
    private GuesthouseEntity guesthouse; // 게스트하우스 ID
}
