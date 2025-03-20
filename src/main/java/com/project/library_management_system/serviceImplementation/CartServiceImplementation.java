package com.project.library_management_system.serviceImplementation;

import com.project.library_management_system.entity.BookEntity;
import com.project.library_management_system.entity.CartEntity;
import com.project.library_management_system.entity.LendingEntity;
import com.project.library_management_system.entity.userEntity;
import com.project.library_management_system.repository.BookRepository;
import com.project.library_management_system.repository.CartRepository;
import com.project.library_management_system.repository.LendingRepository;
import com.project.library_management_system.repository.userRepository;
import com.project.library_management_system.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class CartServiceImplementation implements CartService {
    @Autowired
    userRepository userrepository;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    LendingRepository lendingRepository;
    @Override
    public ResponseEntity<Map<String, Object>> addToCart(Long id) {
        Map<String, Object> response = new HashMap<>();
        String username = getLoggedInUsername();
        Optional<userEntity> userOpt = userrepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            response.put("status", "error");
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        userEntity user = userOpt.get();
        if (cartRepository.countByUser(user) >= 2) {
            response.put("status", "error");
            response.put("message", "Your cart cannot contain more than 2 books");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Optional<BookEntity> bookOpt = bookRepository.findById(id);
        if (bookOpt.isEmpty()) {
            response.put("status", "error");
            response.put("message", "book not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        BookEntity book = bookOpt.get();
        Optional<CartEntity> existingCartItem = cartRepository.findByUserAndBook(user, book);
        if (existingCartItem.isPresent()) {
            response.put("status", "error");
            response.put("message", "Book is already in cart");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        CartEntity cartItem = new CartEntity();
        cartItem.setUser(user);
        cartItem.setBook(book);
        cartRepository.save(cartItem);
        response.put("status", "success");
        response.put("message", "Cart successfully created");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @Override
    public String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            return ((UserDetails)principal).getUsername();
        }
        else{
            return principal.toString();
        }
    }
    @Override
    public ResponseEntity<Map<String,Object>> getCartItems() {
        Map<String,Object> response = new HashMap<>();
        String username = getLoggedInUsername();
        userEntity user = userrepository.findByUsername(username).orElseThrow();
        List<CartEntity> cartItems = cartRepository.findByUser(user);
        if(cartItems.isEmpty()){
            response.put("cartItems", Collections.emptyList());
            response.put("status","success");
            response.put("message","Your cart is empty");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        List<Map<String, Object>> cartResponse = cartItems.stream().map(cartItem -> {
            Map<String, Object> cartMap = new HashMap<>();
            Map<String, Object> bookInfo = new HashMap<>();
            bookInfo.put("id",cartItem.getBook().getId());
            bookInfo.put("title",cartItem.getBook().getTitle());
            bookInfo.put("author",cartItem.getBook().getAuthor());
            bookInfo.put("isbn",cartItem.getBook().getIsbn());
            bookInfo.put("imageUrl",cartItem.getBook().getImage_url());
            cartMap.put("book",bookInfo);
            cartMap.put("addedDate",cartItem.getAddedDate());
            return cartMap;
        }).toList();
        response.put("cartItems", cartResponse);
        response.put("status","success");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @Override
    public ResponseEntity<Map<String, Object>> deleteCartItem(Long id) {
        String username = getLoggedInUsername();
        int deletedCount=cartRepository.deleteByUsernameAndBookId(username,id);
        if(deletedCount>0) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Delete Successful");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else{
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Delete Unsuccessful as item not found in cart");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @Override
    public ResponseEntity<Map<String, Object>> checkout() {
        Map<String, Object> response = new HashMap<>();
        String username = getLoggedInUsername();
        Optional<userEntity> userOpt = userrepository.findByUsername(username);
        userEntity user =  userOpt.get();
        List<LendingEntity> lendingOpt = lendingRepository.findByUser(user);
        for(LendingEntity lending: lendingOpt){
            if(!lending.getStatus().equals("RETURNED")){
                response.put("status","error");
                response.put("message","Please return previously borrowed books to borrow further");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }
        if(userOpt.isEmpty()){
            response.put("status","error");
            response.put("message","User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }


        List<CartEntity> cartItems = cartRepository.findByUser(user);

        if(cartItems.isEmpty()){
            response.put("status","error");
            response.put("message","Your cart is empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        boolean paymentSuccess = true;

        if(!paymentSuccess){
            response.put("status","error");
            response.put("message","Payment failed. Please try again");
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(response);
        }

        List<Map<String, Object>> skippedBooks = new ArrayList<>();
        List<Map<String,Object>> processedBooks = new ArrayList<>();
        for (CartEntity cartItem : cartItems){
            BookEntity book = cartItem.getBook();
            if(book.getCopies_available() <= 0){
                Map<String, Object> skippedBook = new HashMap<>();
                skippedBook.put("bookId", book.getId());
                skippedBook.put("title", book.getTitle());
                skippedBook.put("status", "Out of stock");
                skippedBooks.add(skippedBook);
                continue;
            }
            response.put("skippedBooks", skippedBooks);
            book.setCopies_available(book.getCopies_available() - 1);
            bookRepository.save(book);
//            cartRepository.save(cartItem);

            LendingEntity lending = new LendingEntity();
            lending.setUser(user);
            lending.setBook(book);
            lending.setBorrowDate(LocalDate.now());
            lending.setDueDate(LocalDate.now().plusDays(-2));
            lending.setReturnDate(null);
            lending.setCollected(false);
            lending.setStatus("ACTIVE");
            lendingRepository.save(lending);

            cartRepository.delete(cartItem);

            Map<String,Object> bookDetails = new HashMap<>();
            bookDetails.put("bookId", book.getId());
            bookDetails.put("title", book.getTitle());
            bookDetails.put("status","Checked out");

            processedBooks.add(bookDetails);
        }
        response.put("status","success");
        response.put("message","Checkout is successful. Books borrowed");
        response.put("books", processedBooks);

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

}
