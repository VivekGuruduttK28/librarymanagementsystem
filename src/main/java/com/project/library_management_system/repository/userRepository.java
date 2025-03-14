package com.project.library_management_system.repository;
import org.springframework.stereotype.Repository;
import com.project.library_management_system.entity.userEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface userRepository extends JpaRepository<userEntity, Long> {
    Optional<userEntity> findByUsername(String username);
    Optional<userEntity> findById(Long Id);
}
