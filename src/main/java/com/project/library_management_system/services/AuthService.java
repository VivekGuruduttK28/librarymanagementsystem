package com.project.library_management_system.services;

import com.project.library_management_system.dto.LoginDTO;

public interface AuthService {
    String loginUser(LoginDTO loginDTO);
}
