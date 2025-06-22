package com.example.booksapi.config;

import com.example.booksapi.entity.Author;
import com.example.booksapi.entity.Book;
import com.example.booksapi.repository.AuthorRepository;
import com.example.booksapi.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Configuration class to initialize sample data for testing.
 */
@Configuration
public class DataInitializer {

    /**
     * Initialize sample data for testing.
     *
     * @param authorRepository the author repository
     * @param bookRepository the book repository
     * @return a CommandLineRunner that initializes the data
     */
    @Bean
    public CommandLineRunner initData(AuthorRepository authorRepository, BookRepository bookRepository) {
        return args -> {
            // Create authors
            Author author1 = new Author();
            author1 .setName("J.K. Rowling");
            author1.setAge(56);
            author1.setFollowersNumber(1000);

            Author author2 = new Author();
            author2.setName("George R.R. Martin");
            author2.setAge(73);
            author2.setFollowersNumber(800);

            Author author3 = new Author();
            author3.setName("Stephen King");
            author3.setAge(74);
            author3.setFollowersNumber(1200);

            // Save authors
            List<Author> authors = Arrays.asList(author1, author2, author3);
            authorRepository.saveAll(authors);

            // Create books
            Book book1 = new Book();
            book1.setTitle("Harry Potter and the Philosopher's Stone");
            book1.setAuthor(author1);
            book1.setPublicationDate(LocalDate.of(1997, 6, 26));
            book1.setType("Fantasy");
            book1.setIsbn("9780747532743");

            Book book2 = new Book();
            book2.setTitle("A Game of Thrones");
            book2.setAuthor(author2);
            book2.setPublicationDate(LocalDate.of(1996, 8, 1));
            book2.setType("Fantasy");
            book2.setIsbn("9780553103540");

            Book book3 = new Book();
            book3.setTitle("The Shining");
            book3.setAuthor(author3);
            book3.setPublicationDate(LocalDate.of(1977, 1, 28));
            book3.setType("THRILLER");
            book3.setIsbn("9780385121675");

            Book book4 = new Book();
            book4.setTitle("Harry Potter and the Chamber of Secrets");
            book4.setAuthor(author1);
            book4.setPublicationDate(LocalDate.of(1998, 7, 2));
            book4.setType("FANTASY");
            book4.setIsbn("9780747538486");

            Book book5 = new Book();
            book5.setTitle("A Clash of Kings");
            book5.setAuthor(author2);
            book5.setPublicationDate(LocalDate.of(1998, 11, 16));
            book5.setType("FANTASY");
            book5.setIsbn("9780553108033");

            // Save books
            List<Book> books = Arrays.asList(book1, book2, book3, book4, book5);
            bookRepository.saveAll(books);
        };
    }
}
