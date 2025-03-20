package com.project.library_management_system.model;

import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Fine {
    private Long id;
    private Long userId;
    private Long lendingId;
    private double fineAmount;
    private boolean paid;
    private LocalDate fineDate;
}
