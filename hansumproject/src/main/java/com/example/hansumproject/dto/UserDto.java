package com.example.hansumproject.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class UserDto {
    private Long userId; // 회원ID

    @NotBlank(message = "Username is a required field.")
    private String username; // 로그인 ID

    @NotBlank(message = "Password is a required field.")
    private String password; // 비밀번호

    private String role; // 사용자 권한

    @NotBlank(message = "Name is a required field.")
    private String name; // 이름

    @NotBlank(message = "Phone number is a required field.")
    private String phone; // 전화번호

    @NotBlank(message = "Sex is a required field.")
    private String sex; // 성별

    @NotBlank(message = "birthday is a required field.")
    private String birthday; // 생년월일

    @NotBlank(message = "Nickname is a required field.")
    private String nickname; // 닉네임

    @NotBlank(message = "MBTI is a required field.")
    private String mbti; // MBTI

    @NotNull(message = "UserAgreement is required.")
    private Integer userAgreement; // 이용약관 동의

    private String interestedHobby; //관심사-취미

    private String interestedFood; // 관심사-음식
}
