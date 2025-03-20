package com.project.library_management_system.contollers;

import com.project.library_management_system.entity.userEntity;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.project.library_management_system.model.User;
import com.project.library_management_system.services.userService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;


@RestController
@RequestMapping("/api")
@Validated
public class userControllers {
    
    @Autowired
    userService userservice;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signUpUser(@Valid @RequestBody User user) {
        // return "lol";
        return userservice.createUser(user);
    }
    
    @GetMapping("/users")
    public List<User> displayUsers() {
        return userservice.readUser();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Long id, @Valid @RequestBody User user){
        return userservice.updateUser(id,user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String,Object>> deleteUser(@PathVariable Long id){
        return userservice.deleteUser(id);
    }

    @GetMapping("/user")
    public ResponseEntity<userEntity> getUserDetails(){
        return userservice.getUserDetails();
    }
}
