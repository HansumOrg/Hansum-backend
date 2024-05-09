package com.example.hansumproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FacilityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long facility_id; // 편의시설 ID

    @OneToOne
    @JoinColumn(name = "guesthouse_id", nullable = false)
    private GuesthouseEntity guesthouse; //게스트하우스 ID

    @Column(nullable = false)
    @ColumnDefault("0") // 기본값 0
    private Integer party; // 파티 유무

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer breakfast; // 조식 유무

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer singleroom; // 1인실 유무

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer parking; // 주차장 유무

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer swimmingpool; // 수영장 유무

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer woman_only; // 여성전용
}
