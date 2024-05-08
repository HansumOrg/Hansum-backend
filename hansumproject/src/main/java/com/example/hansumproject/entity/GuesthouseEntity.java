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
    private Long guesthouse_id; // 게스트하우스 ID

    @Column(nullable = false)
    private String guesthouse_name; // 게스트하우스 이름

    @Column(nullable = false)
    private String address; // 주소

    @Column(nullable = false)
    private String location; // 위치(카테고리용)

    @Column(nullable = false)
    private Integer price; // 가격

    @Column(nullable = false)
    private String phone; //전화번호

    @Column(nullable = false)
    @ColumnDefault("0") // 기본값 0
    private float rating; // 평점

    @Column(nullable = false)
    private String imageUrl; // 이미지

    @Column(nullable = false)
    private String mood; // 분위기
}
