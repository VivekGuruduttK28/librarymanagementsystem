package com.project.library_management_system.services;

import com.project.library_management_system.dto.LoginDTO;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AuthService {
    ResponseEntity<Map<String,Object>> loginUser(LoginDTO loginDTO);
}
