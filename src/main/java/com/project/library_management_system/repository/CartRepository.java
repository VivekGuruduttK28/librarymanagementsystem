package com.project.library_management_system.repository;

import com.project.library_management_system.entity.BookEntity;
import com.project.library_management_system.entity.CartEntity;
import com.project.library_management_system.entity.userEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    List<CartEntity> findByUser(userEntity userId);
    Optional<CartEntity> findByUserAndBook(userEntity user, BookEntity book);

    @Transactional
    @Modifying
    @Query("DELETE from CartEntity c WHERE c.user.username = :username AND c.book.id=:bookId")
    int deleteByUsernameAndBookId(@Param("username")String username,@Param("bookId") Long bookId);
}
