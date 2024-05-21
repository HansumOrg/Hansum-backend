package com.example.hansumproject.dto;

import com.example.hansumproject.entity.GuesthouseEntity;
import com.example.hansumproject.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class DibsDto {
    @JsonProperty("dibs_id") // json형식에서 key값 이름 설정
    private Long dibsId; // 찜 ID

    @JsonProperty("guesthouse_id")
    private Long guesthouseId; // 게스트하우스 ID

    @JsonProperty("guesthouse_name")
    private String guesthouseName; // 게스트하우스 이름

    @JsonProperty("address")
    private String address; // 주소

    @JsonProperty("imageBase64")
    private String imageBase64; // 이미지 base64 인코딩 배열
}
