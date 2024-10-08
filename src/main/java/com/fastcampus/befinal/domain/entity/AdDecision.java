package com.fastcampus.befinal.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@Entity(name = "AdDecision")
@Table(name = "ad_decision")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdDecision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "decision", columnDefinition = "varchar(20)")
    private String decision;
}
