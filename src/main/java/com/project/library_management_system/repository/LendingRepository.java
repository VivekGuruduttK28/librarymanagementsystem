package com.project.library_management_system.repository;

import com.project.library_management_system.entity.LendingEntity;
import com.project.library_management_system.entity.userEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LendingRepository extends JpaRepository<LendingEntity, Long> {
    List<LendingEntity> findByUser(userEntity user);
}
