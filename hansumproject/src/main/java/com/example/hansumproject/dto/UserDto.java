package com.example.hansumproject.dto;

import jakarta.persistence.Column;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class UserDto {
    private Long userId; // 회원ID

    private String username; // 로그인 ID

    private String password; // 비밀번호

    private String role; // 사용자 권한

    private String name; // 이름

    private String phone; // 전화번호

    private String sex; // 성별

    private String birthday; // 생년월일

    private String nickname; // 닉네임

    private String mbti; // MBTI

    private Integer userAgreement; // 이용약관 동의

    private String interestedLocation; // 관심사-제주여행지

    private String interestedHobby; //관심사-취미

    private String interestedFood; // 관심사-음식
}
