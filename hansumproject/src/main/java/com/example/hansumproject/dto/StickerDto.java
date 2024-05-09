package com.example.hansumproject.dto;

import com.example.hansumproject.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class StickerDto {
    private Long stickerId; // 스티커 ID

    private UserEntity user; // 회원 ID

    private String stickerText; // 스티커 내용

    private int stickerCount ; // 스티커 개수
}
