package com.project.library_management_system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.library_management_system.entity.BookEntity;
import com.project.library_management_system.model.Book;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<BookEntity,Long> {
    Page<BookEntity> findAll(Pageable pageable);
    Page<BookEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<BookEntity> findByAuthorContainingIgnoreCase(String author, Pageable pageable);
    Optional<BookEntity> findByIsbn(String isbn);
}
