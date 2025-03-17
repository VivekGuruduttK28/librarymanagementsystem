package com.project.library_management_system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="cart")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ManyToOne
    @JoinColumn(name="userId", nullable = false)
    private userEntity user;

    @ManyToOne
    @JoinColumn(name="bookId",referencedColumnName = "id", nullable = false)
    private BookEntity book;

    @Column(nullable = false)
    private LocalDateTime addedDate=LocalDateTime.now();

}
