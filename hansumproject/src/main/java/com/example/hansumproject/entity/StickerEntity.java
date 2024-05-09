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
    private Long sticker_id; // 스티커 ID

//    @ManyToOne
//    @JoinColumn(name= "user_id", nullable = false)
//    private UserEntity user; // 회원 ID
//
//    @Column(nullable = false)
//    private String sticker_text; // 스티커 내용
//
//    @Column(nullable = false)
//    @ColumnDefault("0")
//    private int sticker_cout ; // 스티커 개수
}
