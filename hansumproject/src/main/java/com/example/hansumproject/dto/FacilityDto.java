package com.example.hansumproject.dto;

import com.example.hansumproject.entity.GuesthouseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class FacilityDto {
    private Long facilityId; // 편의시설 ID

    private GuesthouseEntity guesthouse; //게스트하우스 ID

    private Integer party; // 파티 유무

    private Integer breakfast; // 조식 유무

    private Integer singleroom; // 1인실 유무

    private Integer parking; // 주차장 유무

    private Integer swimmingpool; // 수영장 유무

    private Integer womanOnly; // 여성전용
}
