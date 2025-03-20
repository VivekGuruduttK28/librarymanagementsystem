package com.project.library_management_system.services;

import java.util.List;
import java.util.Map;

import com.project.library_management_system.entity.userEntity;
import com.project.library_management_system.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface userService extends UserDetailsService {
    public ResponseEntity<userEntity> getUserDetails();

    ResponseEntity<Map<String, Object>> createUser(User user);
    List<User> readUser();
    ResponseEntity<Map<String, Object>> updateUser(Long id, User user);
    ResponseEntity<Map<String,Object>> deleteUser(Long id);
    public String getLoggedInUsername();

}
