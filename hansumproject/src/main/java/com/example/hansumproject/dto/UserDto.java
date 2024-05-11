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

    @NotBlank(message = "로그인 ID는 필수 입력 값입니다.")
    private String username; // 로그인 ID

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password; // 비밀번호

    private String role; // 사용자 권한

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name; // 이름

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    private String phone; // 전화번호

    @NotBlank(message = "성별은 필수 입력 값입니다.")
    private String sex; // 성별

    @NotBlank(message = "생년월일은 필수 입력 값입니다.")
    private String birthday; // 생년월일

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname; // 닉네임

    @NotBlank(message = "MBTI는 필수 입력 값입니다.")
    private String mbti; // MBTI

    @NotNull(message = "이용약관 동의는 필수입니다.")
    private Integer userAgreement; // 이용약관 동의

    private String interestedHobby; //관심사-취미

    private String interestedFood; // 관심사-음식
}
