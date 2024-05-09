package com.example.hansumproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    @Column(name = "userName", nullable = false)
    private String username; // 로그인 ID

    @Column(name = "password", nullable = false)
    private String password; // 비밀번호

    @Column(name = "role")
    private String role; // 사용자 권한

    @Column(name = "name", nullable = false)
    private String name; // 이름

    @Column(name = "phone", nullable = false)
    private String phone; // 전화번호

    @Column(name = "sex", nullable = false)
    private String sex; // 성별

    @Column(name = "birthday", nullable = false)
    private String birthday; // 생년월일

    @Column(name = "nickname", nullable = false)
    private String nickname; // 닉네임

    @Column(name = "mbit", nullable = false)
    private String mbti; // MBTI

    @Column(name = "userAgreement")
    private Integer userAgreement; // 이용약관 동의

    @Column(name = "interestedLocation")
    private String interestedLocation; // 관심사-제주여행지

    @Column(name = "interestedHobby")
    private String interestedHobby; //관심사-취미

    @Column(name = "interestedFood")
    private String interestedFood; // 관심사-음식
}
