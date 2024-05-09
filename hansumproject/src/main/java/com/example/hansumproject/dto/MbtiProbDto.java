package com.example.hansumproject.dto;

import com.example.hansumproject.entity.GuesthouseEntity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class MbtiProbDto {
    private Long probability_id; // 확률 ID

    private GuesthouseEntity guesthouse; // 게스트하우스 ID

    private String mbti; // MBTI

    private float probability; // 확률
}
