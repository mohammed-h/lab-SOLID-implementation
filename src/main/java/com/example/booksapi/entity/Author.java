package com.example.booksapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing an author in the book collection system.
 */
@Entity
@Table(name = "authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "books")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Author name is required")
    @Column(nullable = false)
    private String name;

    @Min(value = 0, message = "Age must be a positive number")
    private Integer age;

    @Min(value = 0, message = "Followers number must be a positive number")
    @Column(name = "followers_number")
    private Integer followersNumber;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private Set<Book> books = new HashSet<>();

    /**
     * Add a book to this author's collection
     * @param book The book to add
     */
    public void addBook(Book book) {
        books.add(book);
        book.setAuthor(this);
    }

    /**
     * Remove a book from this author's collection
     * @param book The book to remove
     */
    public void removeBook(Book book) {
        books.remove(book);
        book.setAuthor(null);
    }
}
