package com.project.library_management_system.contollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.library_management_system.dto.LoginDTO;
import com.project.library_management_system.services.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class AuthControllers {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO loginDTO) {
        //TODO: process POST request
        return authService.loginUser(loginDTO);
    }
    
}
