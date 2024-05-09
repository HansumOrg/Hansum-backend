package com.example.hansumproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GuesthouseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 1,2,3... 자동 생성
    @Column(name = "guesthouseId")
    private Long guesthouseId; // 게스트하우스 ID

    @Column(name = "guesthouseName", nullable = false)
    private String guesthouseName; // 게스트하우스 이름

    @Column(name = "address", nullable = false)
    private String address; // 주소

    @Column(name = "location", nullable = false)
    private String location; // 위치(카테고리용)

    @Column(name = "price", nullable = false)
    private Integer price; // 가격

    @Column(name = "phone", nullable = false)
    private String phone; //전화번호

    @Column(name = "rating", nullable = false)
    @ColumnDefault("0") // 기본값 0
    private float rating; // 평점

    @Column(name = "imageUrl", nullable = false)
    private String imageUrl; // 이미지

    @Column(name = "mood", nullable = false)
    private String mood; // 분위기
}
