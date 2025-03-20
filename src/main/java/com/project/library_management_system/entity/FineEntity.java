package com.project.library_management_system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "fines")
public class FineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id",nullable = false)
    private userEntity user;
    @ManyToOne
    @JoinColumn(name = "lendingId", referencedColumnName = "id",nullable = false)
    private LendingEntity lending;
    @Column(nullable = false)
    private double fineAmount;
    @Column(nullable = false)
    private boolean paid = false;
    @Temporal(TemporalType.DATE)
    private LocalDate fineDate;
}
