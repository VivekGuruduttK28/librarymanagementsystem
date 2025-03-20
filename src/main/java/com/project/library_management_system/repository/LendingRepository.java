package com.project.library_management_system.repository;

import com.project.library_management_system.dto.LendingDto;
import com.project.library_management_system.entity.LendingEntity;
import com.project.library_management_system.entity.userEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LendingRepository extends JpaRepository<LendingEntity, Long> {
    List<LendingEntity> findByUser(userEntity user);

    List<LendingEntity> findByDueDate(LocalDate remainderDate);
    Optional<LendingEntity> findById(Long id);

    @Query("SELECT new com.project.library_management_system.dto.LendingDto(l.id,u.username,b.title, b.image_url, l.borrowDate, l.dueDate,l.status, f.fineAmount)" +
            "FROM LendingEntity l " +
            "JOIN l.user u " +
            "JOIN l.book b " +
            "LEFT JOIN FineEntity f ON l.id = f.lending.id")
    List<LendingDto> getAllLendings();

    @Query("SELECT COUNT(l) FROM LendingEntity l WHERE l.status = 'OVERDUE'")
    long countOverdueBooks();

    List<LendingEntity> findByUserAndStatus(userEntity user, String status);
}
