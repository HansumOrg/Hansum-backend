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
    @Column(name = "facilityId")
    private Long facilityId; // 편의시설 ID

    @OneToOne
    @JoinColumn(name = "guesthouseId", nullable = false)
    private GuesthouseEntity guesthouse; //게스트하우스 ID

    @Column(name = "party", nullable = false)
    @ColumnDefault("0") // 기본값 0
    private Integer party; // 파티 유무

    @Column(name = "breakfast", nullable = false)
    @ColumnDefault("0")
    private Integer breakfast; // 조식 유무

    @Column(name = "singleroom", nullable = false)
    @ColumnDefault("0")
    private Integer singleroom; // 1인실 유무

    @Column(name = "parking", nullable = false)
    @ColumnDefault("0")
    private Integer parking; // 주차장 유무

    @Column(name = "swimmingpool", nullable = false)
    @ColumnDefault("0")
    private Integer swimmingpool; // 수영장 유무

    @Column(name = "womanOnly", nullable = false)
    @ColumnDefault("0")
    private Integer womanOnly; // 여성전용
}
