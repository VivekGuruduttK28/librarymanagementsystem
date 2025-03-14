package com.project.library_management_system.model;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lending {
    private Long id;
    private Long userId;
    private Long bookId;
    private Date borrowDate;
    private Date returnDate;
    private String status;
}
