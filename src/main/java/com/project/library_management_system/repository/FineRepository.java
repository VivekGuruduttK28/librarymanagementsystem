package com.project.library_management_system.repository;

import com.project.library_management_system.entity.FineEntity;
import com.project.library_management_system.entity.userEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface FineRepository extends JpaRepository<FineEntity, Long> {
    List<FineEntity> findByUserId(Long userId);
    Optional<FineEntity> findByLendingId(Long id);

    List<FineEntity> findByUser(userEntity user);
}
