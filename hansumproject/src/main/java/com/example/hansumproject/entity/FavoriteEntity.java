package com.example.hansumproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FavoriteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dibs_id; // 찜 ID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // 회원 ID

    @ManyToOne
    @JoinColumn(name = "guesthouse_id", nullable = false)
    private GuesthouseEntity guesthouse; // 게스트하우스 ID
}
