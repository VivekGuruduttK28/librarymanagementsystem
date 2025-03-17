package com.project.library_management_system.contollers;

import com.project.library_management_system.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    @Autowired
    private EmailService emailService;
    @GetMapping
    public void sendMail(){
        emailService.sendRemainderEmail("vivek20204@gmail.com","alchemy", LocalDate.of(2025, 3,18));
        System.out.println("TRIGGERED");
    }
}
