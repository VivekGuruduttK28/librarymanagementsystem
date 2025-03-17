package com.project.library_management_system.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name="lending")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LendingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private userEntity user;

    @ManyToOne
    @JoinColumn(name = "bookId",referencedColumnName = "id", nullable = false)
    private BookEntity book;

    @Temporal(TemporalType.DATE)
    private LocalDate borrowDate;

    @Temporal(TemporalType.DATE)
    private LocalDate returnDate;

    @Temporal(TemporalType.DATE)
    private LocalDate dueDate;

    @Column
    private boolean collected = false;

    @Column
    private String status = "ACTIVE";
}
