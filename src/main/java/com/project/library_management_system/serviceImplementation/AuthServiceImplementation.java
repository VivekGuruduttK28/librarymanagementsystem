package com.project.library_management_system.serviceImplementation;

import java.util.Optional;

import com.project.library_management_system.services.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public String loginUser(LoginDTO loginDTO) {
//        Optional<userEntity> userOpt = userrepository.findByUsername(loginDTO.getUsername());
//        if(userOpt.isPresent() && passwordEncoder.matches(loginDTO.getPassword(),userOpt.get().getPassword())){
//            return "Login Successfull";
//        }
//        throw new RuntimeException("Invalid email or password");
        Authentication authentication =
                authManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginDTO.getUsername(),loginDTO.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(loginDTO.getUsername());
        }
        else{
            return "Failed";
        }

        // return "Please try with proper credentials";
        
    }
    
}
