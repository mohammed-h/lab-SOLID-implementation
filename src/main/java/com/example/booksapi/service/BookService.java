package com.example.booksapi.service;

import com.example.booksapi.dto.AuthorDTO;
import com.example.booksapi.dto.BookRequestDTO;
import com.example.booksapi.dto.BookResponseDTO;
import com.example.booksapi.entity.Book;

import java.util.List;
import java.util.Set;

/**
 * Interface for Book operations.
 */
public interface BookService {

    /**
     * Create a new book.
     *
     * @param bookRequestDTO the book to create
     * @return the created book
     */
    BookResponseDTO createBook(BookRequestDTO bookRequestDTO);

    BookResponseDTO updateBook(Long id, BookRequestDTO bookRequestDTO);

    /**
     * Get a book by ID.
     *
     * @param id the ID of the book
     * @return an Optional containing the book if found, or empty if not found
     */
    BookResponseDTO getBookById(Long id);

    /**
     * Get a book by title.
     *
     * @param title the title of the book
     * @return an Optional containing the book if found, or empty if not found
     */
    List<BookResponseDTO> getBookByTitle(String title);


    /**
     * Delete a book by ID.
     *
     * @param id the ID of the book to delete
     */
    void deleteBook(Long id);

    /**
     * Rate a book on a scale of 1-10 based on publication date and author.
     * More recent books and books by authors with more followers get higher ratings.
     *
     * @param bookId the ID of the book to rate
     * @return the rating of the book on a scale of 1-10, or -1 if the book was not found
     */
    double rateBook(Long bookId);

    /**
     * Get all unique authors for a list of book IDs.
     *
     * @param bookIds the list of book IDs
     * @return a set of unique authors for the given book IDs
     */
    List<AuthorDTO> getUniqueAuthorsByBookIds(List<Long> bookIds);
}