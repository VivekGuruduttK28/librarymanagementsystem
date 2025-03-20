package com.project.library_management_system.serviceImplementation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.project.library_management_system.services.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.library_management_system.dto.LoginDTO;
import com.project.library_management_system.repository.userRepository;
import com.project.library_management_system.services.AuthService;

import com.project.library_management_system.entity.userEntity;

@Service
public class AuthServiceImplementation implements AuthService{

    @Autowired
    private userRepository userrepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthenticationManager authManager;

    @Override
    public ResponseEntity<Map<String, Object>> loginUser(LoginDTO loginDTO) {
        Map<String, Object> response = new HashMap<>();
        Authentication authentication =
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginDTO.getUsername(),loginDTO.getPassword()));
        if(authentication.isAuthenticated()){
            Optional<userEntity> userOpt = userrepository.findByUsername(loginDTO.getUsername());
            userEntity user = userOpt.get();
            String role = user.getRole();
            String token= jwtService.generateToken(loginDTO.getUsername());

            response.put("status","success");
            response.put("message", "Login successful");
            response.put("token", token);
            response.put("role",role);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        else{
            response.put("status","failed");
            response.put("message", "Authentication failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
}
