package com.project.library_management_system.contollers;

import com.project.library_management_system.services.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/fines")
public class FineController {
    @Autowired
    FineService fineService;
    @GetMapping
    public void updateFines() {
        fineService.updateFinesDaily();
    }
    @PostMapping("/payfine")
    public ResponseEntity<Map<String, Object>> payFine(){
        return fineService.payFine();
    }

    @GetMapping("/totalfine")
    public ResponseEntity<Map<String,Object>> totalFine(){
        return fineService.totalFine();
    }
}
