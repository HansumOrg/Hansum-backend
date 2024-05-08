package com.example.hansumproject.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class UserDto {
    private Long userId; // 회원 ID

    private String loginId; // 로그인 ID

    private String password; // 비밀번호

    private String username; // 이름

    private String phone; // 전화번호

    private String sex; // 성별

    private String birthday; // 생년월일

    private String nickname; // 닉네임

    private String mbti; // MBTI

    private Integer user_agreement; // 이용약관 동의

    private String interested_location; // 관심사-제주여행지

    private String interested_hobby; //관심사-취미

    private String interested_food; // 관심사-음식
}
