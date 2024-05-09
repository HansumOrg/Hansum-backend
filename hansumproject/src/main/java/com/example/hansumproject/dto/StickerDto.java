package com.example.hansumproject.dto;

import com.example.hansumproject.entity.UserEntity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class StickerDto {
    private Long sticker_id; // 스티커 ID

    private UserEntity user; // 회원 ID

    private String sticker_text; // 스티커 내용

    private int sticker_cout ; // 스티커 개수
}
