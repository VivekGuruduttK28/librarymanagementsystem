package com.project.library_management_system.contollers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.library_management_system.model.Book;
import com.project.library_management_system.services.BookService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/books")
public class BookController {
    
    @Autowired
    BookService bookService;

    @PostMapping
    public Book createBook(@RequestBody Book book) {
    
        return bookService.saveBook(book);
    }

    @GetMapping
    public Page<Book> getAllBooks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size ) {
        return bookService.getAllBooks(page, size);
    }
    
    @GetMapping("/{id}")
    public Optional<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }
    
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        //TODO: process PUT request
        return bookService.updateBook(id, book);
    }
    
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
    }

    @GetMapping("/search")
    public Page<Book> searchBooks(@RequestParam String query, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return bookService.searchBook(query, page, size);
    }
    
}
