package com.example.booksapi.controller;

import com.example.booksapi.dto.AuthorDTO;
import com.example.booksapi.dto.BookRequestDTO;
import com.example.booksapi.dto.BookResponseDTO;
import com.example.booksapi.service.BookService;
import com.example.booksapi.service.ExternalBookService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;

/**
 * REST controller for book operations.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final ExternalBookService externalBookService;

    @Autowired
    public BookController(BookService bookService, ExternalBookService externalBookService) {
        this.bookService = bookService;
        this.externalBookService = externalBookService;
    }

    /**
     * Create a new book.
     *
     * @param bookRequestDTO the book to create
     * @return the URI of created book
     */
    @PostMapping
    private ResponseEntity<Void> createBook(@RequestBody BookRequestDTO bookRequestDTO, UriComponentsBuilder ucb) {
        BookResponseDTO createdBook = bookService.createBook(bookRequestDTO);
        URI locationOfNewBook = ucb
                    .path("api/books/{id}")
                    .buildAndExpand(createdBook.getId())
                    .toUri();
        return ResponseEntity.created(locationOfNewBook).build();
    }

    /**
     * Update a book.
     *
     * @param id the ID of the book to update
     * @param bookRequestDTO the updated book details
     * @return the updated book if found, or 404 if not found
     */
    @PutMapping("/{id}")
    private ResponseEntity<Void> putBook(@PathVariable Long id, @RequestBody BookRequestDTO bookRequestDTO) {
        bookService.updateBook(id, bookRequestDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get a book by ID.
     *
     * @param id the ID of the book
     * @return the book if found, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id) {
        BookResponseDTO bookResponseDTO = bookService.getBookById(id);
        return ResponseEntity.ok(bookResponseDTO);
    }

    /**
     * Get a book by title.
     *
     * @param title the title of the book
     * @return the book if found, or 404 if not found
     */
    @GetMapping("/search")
    public ResponseEntity<List<BookResponseDTO>> getBookByTitle(@RequestParam String title) {
        List<BookResponseDTO> books = bookService.getBookByTitle(title);
        return ResponseEntity.ok(books);
    }

    /**
     * Delete a book.
     *
     * @param id the ID of the book to delete
     * @return 204 No Content if successful, or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        BookResponseDTO bookResponseDTO = bookService.getBookById(id);;
        if(bookResponseDTO != null) {
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Rate a book on a scale of 1-10.
     *
     * @param id the ID of the book to rate
     * @return the rating of the book on a scale of 1-10, or 404 if the book was not found
     */
    @GetMapping("/{id}/rating")
    public ResponseEntity<Double> getRateBook(@PathVariable Long id) {
        double rating = bookService.rateBook(id);
        if (rating < 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(rating, HttpStatus.OK);
    }

    /**
     * Get all unique authors for a list of book IDs.
     *
     * @param bookIds the list of book IDs
     * @return a set of unique authors for the given book IDs
     */
    @GetMapping("/authors")
    public ResponseEntity<List<AuthorDTO>> getUniqueAuthorsByBookIds(@RequestParam List<Long> bookIds) {
        List<AuthorDTO> authors = bookService.getUniqueAuthorsByBookIds(bookIds);
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    /**
     * Find a book by ISBN using the OpenLibrary API.
     *
     * @param isbn the ISBN of the book to find
     * @return the book information if found, or 404 if not found
     */
    @GetMapping("/isbn/{isbn}")
    public Mono<ResponseEntity<JsonNode>> findBookByIsbn(@PathVariable String isbn) {
        return externalBookService.findBookByIsbn(isbn)
                .filter(bookInfo -> bookInfo != null)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.<JsonNode>notFound().build()));
    }
}
