package com.project.library_management_system.services;

import com.project.library_management_system.model.Book;

import java.util.Optional;

import org.springframework.data.domain.Page;

public interface BookService {
    Book saveBook(Book book);
    Page<Book> getAllBooks(int page, int size);
    Optional<Book> getBookById(Long id);
    Book updateBook(Long id, Book updatedBook);
    void deleteBook(Long id);
    Page<Book> searchBook(String query, int page, int size);
}
