package com.example.hansumproject.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class GuesthouseDto {
    private Long guesthouseId; // 게스트하우스 ID

    private String guesthouseName; // 게스트하우스 이름

    private String address; // 주소

    private String location; // 위치(카테고리용)

    private Integer price; // 가격

    private String phone; //전화번호

    private float rating; // 평점

    private String imageUrl; // 이미지

    private String mood; // 분위기
}
