package com.project.library_management_system.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    private Long cartId;
    private Long userId;
    private Long bookId;
    private LocalDateTime addedDate;
}
