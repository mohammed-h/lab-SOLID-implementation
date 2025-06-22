package com.example.booksapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDTO {
    private Long id;
    private String title;
    private Long authorId;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate publicationDate;
    private String type;
}