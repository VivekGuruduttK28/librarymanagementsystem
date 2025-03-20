package com.project.library_management_system.contollers;

import com.project.library_management_system.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @PostMapping("/add/{id}")
    public ResponseEntity<Map<String,Object>> addToCart(@PathVariable Long id){
        return cartService.addToCart(id);
    }
    @GetMapping("/display")
    public ResponseEntity<Map<String,Object>> getCartItems(){
        return cartService.getCartItems();
    }
    @DeleteMapping("/removeItem/{id}")
    public ResponseEntity<Map<String,Object>> deleteCartItem(@PathVariable Long id){
        return cartService.deleteCartItem(id);
    }

    @PostMapping("/checkout")
    public ResponseEntity<Map<String, Object>> checkout() {
        return cartService.checkout();
    }

}
