package com.example.booksapi.service;

import com.example.booksapi.entity.Author;
import com.example.booksapi.entity.Book;
import com.example.booksapi.repository.BookRepository;
import com.example.booksapi.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BookRatingService.
 * Following the Red-Green-Refactor approach.
 */
@ExtendWith(MockitoExtension.class)
class BookRatingServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Author popularAuthor;
    private Author unpopularAuthor;
    private Book recentBook;
    private Book oldBook;

    @BeforeEach
    void setUp() {
        // Create test authors
        popularAuthor = new Author();
        popularAuthor.setId(1L);
        popularAuthor.setName("Popular Author");
        popularAuthor.setAge(40);
        popularAuthor.setFollowersNumber(1000); // Many followers

        unpopularAuthor = new Author();
        unpopularAuthor.setId(2L);
        unpopularAuthor.setName("Unpopular Author");
        unpopularAuthor.setAge(60);
        unpopularAuthor.setFollowersNumber(100); // Few followers

        // Create test books
        recentBook = new Book();
        recentBook.setId(1L);
        recentBook.setTitle("Recent Book");
        recentBook.setAuthor(popularAuthor);
        recentBook.setPublicationDate(LocalDate.now().minusYears(1)); // Published 1 year ago
        recentBook.setType("FICTION");

        oldBook = new Book();
        oldBook.setId(2L);
        oldBook.setTitle("Old Book");
        oldBook.setAuthor(unpopularAuthor);
        oldBook.setPublicationDate(LocalDate.now().minusYears(20)); // Published 20 years ago
        oldBook.setType("HISTORY");
    }

    @Test
    @DisplayName("Should return high rating for recent book by popular author")
    void shouldReturnHighRatingForRecentBookByPopularAuthor() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(recentBook));

        double rating = bookService.rateBook(1L);

        assertTrue(rating >= 7.0, "Rating should be high for a recent book by a popular author");
        assertTrue(rating <= 10.0, "Rating should not exceed 10");
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return low rating for old book by unpopular author")
    void shouldReturnLowRatingForOldBookByUnpopularAuthor() {
        when(bookRepository.findById(2L)).thenReturn(Optional.of(oldBook));

        double rating = bookService.rateBook(2L);

        assertTrue(rating >= 1.0, "Rating should be at least 1");
        assertTrue(rating < 5.0, "Rating should be low for an old book by an unpopular author");
        verify(bookRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Should return -1 for non-existent book")
    void shouldReturnNegativeOneForNonExistentBook() {
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        double rating = bookService.rateBook(999L);

        assertEquals(-1.0, rating, "Rating should be -1 for a non-existent book");
        verify(bookRepository, times(1)).findById(999L);
    }
}