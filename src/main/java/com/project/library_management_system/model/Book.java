package com.project.library_management_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Book {
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String image_url;
    private int copies_available;
}
