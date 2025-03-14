package com.project.library_management_system.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;

    @NotBlank(message="Username cannot be blank")
    private String username;

    @NotBlank(message="Phone number cannot be blank")
    @Pattern(regexp = "^\\d{10}$", message="Phone number must be 10 digits")
    private String phone;

    @NotBlank(message="Email cannot be blank")
    @Email(message="Invalid email address")
    private String email;


    private String role;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    @NotBlank(message = "Password cannot be blank")
    private String password;

}
