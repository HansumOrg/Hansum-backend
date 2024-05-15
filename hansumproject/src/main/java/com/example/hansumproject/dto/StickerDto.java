package com.example.hansumproject.dto;

import com.example.hansumproject.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class StickerDto {
    private Long userId; // 받는 사람의 회원 ID

    private List<String> stickerTexts; // 스티커 내용
}
