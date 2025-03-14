package com.project.library_management_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import lombok.Data;

@Data
@Entity
@Table(name="users")
public class userEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false,unique=true)
    private String email;

    @Column
    private String role = "USER";

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String password;

    
}
