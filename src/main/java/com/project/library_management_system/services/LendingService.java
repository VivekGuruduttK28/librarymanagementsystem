package com.project.library_management_system.services;

import com.project.library_management_system.dto.LendingDto;
import com.project.library_management_system.entity.LendingEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface LendingService {
    public List<LendingEntity> getAllLendings();
    public ResponseEntity<Map<String, Object>> markAsCollected(Long lendingId);
    public ResponseEntity<Map<String, Object>> markAsReturned(Long lendingId);
    public List<LendingDto> displayLendingsDetails();

    public int getBorrowedBooks();

    public long getTotalOverdueBooks();

    String getLoggedInUsername();

    ResponseEntity<List<LendingDto>> getHistory();

    ResponseEntity<Integer> getHistoryCount();

    int getOverdueBooks();
}
