package com.example.booksapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookMessageDTO {

    private Long bookId;
    private String title;
    private String author;
    private String date;
}