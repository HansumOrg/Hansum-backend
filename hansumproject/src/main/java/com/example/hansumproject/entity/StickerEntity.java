package com.example.hansumproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StickerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stickerId")
    private Long stickerId; // 스티커 ID

    @ManyToOne
    @JoinColumn(name= "userId", nullable = false)
    private UserEntity user; // 회원 ID

    @Column(name = "stickerText", nullable = false)
    private String stickerText; // 스티커 내용

    @Column(name = "stickerCount", nullable = false)
    @ColumnDefault("0")
    private int stickerCount ; // 스티커 개수
}
