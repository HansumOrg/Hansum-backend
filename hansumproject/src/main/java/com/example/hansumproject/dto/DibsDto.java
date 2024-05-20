package com.example.hansumproject.dto;

import com.example.hansumproject.entity.GuesthouseEntity;
import com.example.hansumproject.entity.UserEntity;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class DibsDto {
    private Long dibsId; // 찜 ID

    private UserEntity user; // 회원 ID

    private GuesthouseEntity guesthouse; // 게스트하우스 ID
}
