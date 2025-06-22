package com.example.booksapi.repository;

import com.example.booksapi.entity.Author;
import com.example.booksapi.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Repository for Book entity providing CRUD operations.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    /**
     * Find a book by title.
     * 
     * @param title the title of the book
     * @return an Optional containing the book if found, or empty if not found
     */
    List<Book> findByTitle(String title);

    /**
     * Find all unique authors for a list of book IDs.
     * 
     * @param bookIds the list of book IDs
     * @return a set of unique authors for the given book IDs
     */
    @Query("SELECT DISTINCT b.author FROM Book b WHERE b.id IN :bookIds")
    List<Author> findUniqueAuthorsByBookIds(@Param("bookIds") List<Long> bookIds);
}