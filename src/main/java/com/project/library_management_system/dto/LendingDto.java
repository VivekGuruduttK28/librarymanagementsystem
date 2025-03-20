package com.project.library_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LendingDto {
    private Long lendingId;
    private String username;
    private String bookName;
    private String imageUrl;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private String status;
    private double fineAmount;

    public LendingDto(Long lendingId, String bookName, String imageUrl, LocalDate borrowDate, LocalDate dueDate, String status) {
        this.lendingId = lendingId;
        this.bookName = bookName;
        this.imageUrl = imageUrl;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = status;
    }

}
