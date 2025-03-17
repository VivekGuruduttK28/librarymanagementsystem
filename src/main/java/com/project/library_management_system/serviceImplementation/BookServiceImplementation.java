package com.project.library_management_system.serviceImplementation;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.project.library_management_system.entity.BookEntity;
import com.project.library_management_system.model.Book;
import com.project.library_management_system.repository.BookRepository;
import com.project.library_management_system.services.BookService;

@Service
public class BookServiceImplementation implements BookService {
    @Autowired
    BookRepository bookrepository;

    @Override
    public Book saveBook(Book book) {
        if (book == null || book.getIsbn() == null) {
            throw new IllegalArgumentException("Book or ISBN cannot be null");
        }
        Optional<BookEntity> existingBookOpt = bookrepository.findByIsbn(book.getIsbn());
        BookEntity bookEntity;
        if (existingBookOpt.isPresent()) {
            bookEntity = existingBookOpt.get();
            int currentCopies = bookEntity.getCopies_available();
            int newCopies = book.getCopies_available();
            bookEntity.setCopies_available(currentCopies + newCopies);
        }
        else {
            bookEntity = new BookEntity();
            BeanUtils.copyProperties(book, bookEntity);
            bookEntity.setCopies_available(book.getCopies_available() > 0 ? book.getCopies_available() : 0);
        }

        BookEntity savedEntity = bookrepository.save(bookEntity);
        Book savedBook = new Book();
        BeanUtils.copyProperties(savedEntity, savedBook);
        return savedBook;
    }


    @Override
    public Page<Book> getAllBooks(int page, int size) {
        Page<BookEntity> bookEntities = bookrepository.findAll(PageRequest.of(page, size));
        return bookEntities.map(entity -> {
            Book book = new Book();
            BeanUtils.copyProperties(entity, book);
            return book;
        });
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return bookrepository.findById(id).map(entity -> {
            Book book = new Book();
            BeanUtils.copyProperties(entity, book);
            return book;
        });
    }

    @Override
    public Book updateBook(Long id, Book updatedBook) {
        return bookrepository.findById(id)
            .map(bookEntity -> {
                bookEntity.setTitle(updatedBook.getTitle());
                bookEntity.setAuthor(updatedBook.getAuthor());
                bookEntity.setIsbn(updatedBook.getIsbn());
                bookEntity.setCopies_available(updatedBook.getCopies_available());
                bookEntity.setImage_url(updatedBook.getImage_url()); 
                BookEntity updatedEntity = bookrepository.save(bookEntity);

                Book book = new Book();
                BeanUtils.copyProperties(updatedEntity, book);
                return book;
            })
            .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }

    @Override
    public void deleteBook(Long id) {
        bookrepository.deleteById(id);
    }

    @Override
    public Page<Book> searchBook(String query, int page, int size) {
        Page<BookEntity> bookEntities = bookrepository.findByTitleContainingIgnoreCase(query, PageRequest.of(page, size));
        return bookEntities.map(entity -> {
            Book book = new Book();
            BeanUtils.copyProperties(entity, book);
            return book;
        });
    }
}
