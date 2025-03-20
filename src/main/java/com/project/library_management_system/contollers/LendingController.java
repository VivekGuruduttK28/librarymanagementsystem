package com.project.library_management_system.contollers;

import com.project.library_management_system.dto.LendingDto;
import com.project.library_management_system.entity.LendingEntity;
import com.project.library_management_system.services.LendingService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lending")
public class LendingController {

    @Autowired
    private LendingService lendingService;
    @GetMapping
    public List<LendingEntity> getAllLendings(){
        return lendingService.getAllLendings();
    }
    @PostMapping("/collect/{id}")
    public ResponseEntity<Map<String,Object>> collectBook(@PathVariable Long id){
        return lendingService.markAsCollected(id);
    }

    @PostMapping("/return/{id}")
    public ResponseEntity<Map<String, Object>> returnBook(@PathVariable Long id){
        return lendingService.markAsReturned(id);
    }

    @GetMapping("/getAllLendings")
    public ResponseEntity<List<LendingDto>> displayLendingsDetails(){
        return ResponseEntity.ok(lendingService.displayLendingsDetails());
    }

    @GetMapping("/borrowedBooks/count/total")
    public ResponseEntity<Integer> getBorrowedBooksTotal(){
        return ResponseEntity.ok(lendingService.getBorrowedBooks());
    }

    @GetMapping("/overdue/count/total")
    public ResponseEntity<Long> getOverDueBooksTotal(){
        return ResponseEntity.ok(lendingService.getTotalOverdueBooks());
    }

    @GetMapping("/history")
    public ResponseEntity<List<LendingDto>> getHistory(){
        return lendingService.getHistory();
    }

    @GetMapping("/history/count")
    public ResponseEntity<Integer> getHistoryCount(){
        return lendingService.getHistoryCount();
    }

        @GetMapping("overdue/count")
    public ResponseEntity<Integer> getOverDueBooks(){
        return ResponseEntity.ok(lendingService.getOverdueBooks());
    }


}
