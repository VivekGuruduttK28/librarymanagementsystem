package com.project.library_management_system.services;


import com.project.library_management_system.entity.CartEntity;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.List;

public interface CartService {
    ResponseEntity<Map<String, Object>> addToCart(Long Id);
    ResponseEntity<Map<String,Object>> getCartItems();
    String getLoggedInUsername();
    ResponseEntity<Map<String, Object>> deleteCartItem(Long id);
    ResponseEntity<Map<String,Object>> checkout();
}
