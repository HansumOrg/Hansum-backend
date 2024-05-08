package com.example.hansumproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor // 모든 필드를 매개변수로 갖는 생성자 자동 생성
@NoArgsConstructor // 매개변수가 아예 없는 기본 생성자 자동 생성
@ToString
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 1,2,3... 자동 생성
    private Long userId; // 회원 ID

    @Column(nullable = false)
    private String loginId; // 로그인 ID

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false)
    private String username; // 이름

    @Column(nullable = false)
    private String phone; // 전화번호

    @Column(nullable = false)
    private String sex; // 성별

    @Column(nullable = false)
    private String birthday; // 생년월일

    @Column(nullable = false)
    private String nickname; // 닉네임

    @Column(nullable = false)
    private String mbti; // MBTI

    @Column
    private Integer user_agreement; // 이용약관 동의

    @Column
    private String interested_location; // 관심사-제주여행지

    @Column
    private String interested_hobby; //관심사-취미

    @Column
    private String interested_food; // 관심사-음식
}
