package com.project.library_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name="lending")
@Data
@Getter
@Setter
public class LendingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private userEntity user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

    @Temporal(TemporalType.DATE)
    private Date borrowDate;

    @Temporal(TemporalType.DATE)
    private Date returnDate;

    @Column
    private String Status;
}
