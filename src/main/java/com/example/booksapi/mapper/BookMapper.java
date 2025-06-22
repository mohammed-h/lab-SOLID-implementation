package com.example.booksapi.mapper;

import com.example.booksapi.dto.AuthorDTO;
import com.example.booksapi.dto.BookRequestDTO;
import com.example.booksapi.dto.BookResponseDTO;
import com.example.booksapi.entity.Author;
import com.example.booksapi.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between Book entity and BookDTO.
 */
@Component
public class BookMapper {

    private final AuthorMapper authorMapper;

    @Autowired
    public BookMapper(AuthorMapper authorMapper) {
        this.authorMapper = authorMapper;
    }

    /**
     * Converts a Book entity to a BookDTO.
     *
     * @param book the Book entity to convert
     * @return the corresponding BookDTO
     */
    public BookResponseDTO toResponseDTO(Book book) {
        if (book == null) {
            return null;
        }
        BookResponseDTO bookResponseDTO = new BookResponseDTO();
        bookResponseDTO.setId(book.getId());
        bookResponseDTO.setTitle(book.getTitle());
        bookResponseDTO.setPublicationDate(book.getPublicationDate());
        bookResponseDTO.setType(book.getType());

        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(book.getAuthor().getId());
        authorDTO.setName(book.getAuthor().getName());
        authorDTO.setAge(book.getAuthor().getAge());
        authorDTO.setFollowersNumber(book.getAuthor().getFollowersNumber());

        bookResponseDTO.setAuthor(authorDTO);
        return bookResponseDTO;
    }


    public Book toEntity(BookRequestDTO dto, Author author) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setPublicationDate(dto.getPublicationDate());
        book.setType(dto.getType());
        book.setAuthor(author);
        return book;
    }

    public Book updateEntityFromDTO(Book book, BookRequestDTO bookRequestDTO, Author author) {
        if (book == null || bookRequestDTO == null) {
            return book;
        }

        if (bookRequestDTO.getTitle() != null) {
            book.setTitle(bookRequestDTO.getTitle());
        }

        if (bookRequestDTO.getAuthorId() != null) {
            book.setAuthor(author);
        }

        if (bookRequestDTO.getPublicationDate() != null) {
            book.setPublicationDate(bookRequestDTO.getPublicationDate());
        }

        if (bookRequestDTO.getType() != null) {
            book.setType(bookRequestDTO.getType());
        }
        return book;
    }
}