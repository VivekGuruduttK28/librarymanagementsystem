package com.project.library_management_system.services;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface FineService {
    public void updateFinesDaily();

    public String getLoggedInUsername();
    public ResponseEntity<Map<String, Object>> payFine();

    ResponseEntity<Map<String,Object>> totalFine();
}
