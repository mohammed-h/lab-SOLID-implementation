package com.example.booksapi.service.impl;

import com.example.booksapi.dto.AuthorDTO;
import com.example.booksapi.dto.BookRequestDTO;
import com.example.booksapi.dto.BookResponseDTO;
import com.example.booksapi.entity.Author;
import com.example.booksapi.entity.Book;
import com.example.booksapi.exception.AuthorNotFoundException;
import com.example.booksapi.exception.BookNotFoundException;
import com.example.booksapi.mapper.AuthorMapper;
import com.example.booksapi.mapper.BookMapper;
import com.example.booksapi.repository.AuthorRepository;
import com.example.booksapi.repository.BookRepository;
import com.example.booksapi.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of BookService interface.
 * Handles basic CRUD operations for books.
 */
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;
    private final AuthorMapper authorMapper;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository, BookMapper bookMapper, AuthorMapper authorMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.bookMapper = bookMapper;
        this.authorMapper = authorMapper;
    }

    @Override
    @Transactional
    public BookResponseDTO createBook(BookRequestDTO bookRequestDTO) {
        Author author = authorRepository.findById(bookRequestDTO.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException(bookRequestDTO.getAuthorId()));
        Book book = bookMapper.toEntity(bookRequestDTO, author);
        return bookMapper.toResponseDTO(bookRepository.save(book));
    }

    @Override
    @Transactional
    public BookResponseDTO updateBook(Long id, BookRequestDTO bookRequestDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id.toString()));
        Author author = authorRepository.findById(bookRequestDTO.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException(bookRequestDTO.getAuthorId()));

        Book updatedBook = bookMapper.updateEntityFromDTO(book, bookRequestDTO, author);
        return bookMapper.toResponseDTO(bookRepository.save(updatedBook));
    }

    @Override
    public BookResponseDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id.toString()));
        return  bookMapper.toResponseDTO(book);
    }

    @Override
    public List<BookResponseDTO> getBookByTitle(String title) {
        List<Book> books = bookRepository.findByTitle(title);
        return books.stream()
                .map(bookMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<AuthorDTO> getUniqueAuthorsByBookIds(List<Long> bookIds) {
        List<Author> authors = bookRepository.findUniqueAuthorsByBookIds(bookIds);
        return authors.stream()
                .map(authorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public double rateBook(Long bookId) {
        return bookRepository.findById(bookId)
                .map(this::calculateBookRating)
                .orElse(-1.0); // Return -1 if book not found
    }

    /**
     * Calculate the rating for a book based on publication date and author popularity.
     *
     * @param book the book to rate
     * @return the calculated rating on a scale of 1-10
     */
    private double calculateBookRating(Book book) {
        double dateScore = calculateDateScore(book.getPublicationDate());
        double followersScore = calculateFollowersScore(book.getAuthor());

        // Combine scores and ensure it's between 1 and 10
        return Math.max(1, Math.min(10, dateScore + followersScore));
    }

    /**
     * Calculate the score based on publication date.
     * More recent books get higher scores.
     *
     * @param publicationDate the publication date of the book
     * @return the date score (maximum 5 points)
     */
    private double calculateDateScore(LocalDate publicationDate) {
        LocalDate now = LocalDate.now();
        long yearsSincePublication = ChronoUnit.YEARS.between(publicationDate, now);
        return Math.max(0, 5 - (yearsSincePublication * 0.25));
    }

    /**
     * Calculate the score based on author's followers.
     * Authors with more followers get higher scores.
     *
     * @param author the author of the book
     * @return the followers score (maximum 5 points)
     */
    private double calculateFollowersScore(Author author) {
        return Math.min(5, author.getFollowersNumber() / 200.0);
    }


}