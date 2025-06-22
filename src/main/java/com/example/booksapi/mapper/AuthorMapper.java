package com.example.booksapi.mapper;

import com.example.booksapi.dto.AuthorDTO;
import com.example.booksapi.entity.Author;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between Author entity and AuthorDTO.
 */
@Component
public class AuthorMapper {

    /**
     * Converts an Author entity to an AuthorDTO.
     *
     * @param author the Author entity to convert
     * @return the corresponding AuthorDTO
     */
    public AuthorDTO toDTO(Author author) {
        if (author == null) {
            return null;
        }
        
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setName(author.getName());
        authorDTO.setAge(author.getAge());
        authorDTO.setFollowersNumber(author.getFollowersNumber());
        
        return authorDTO;
    }

    /**
     * Converts an AuthorDTO to an Author entity.
     *
     * @param authorDTO the AuthorDTO to convert
     * @return the corresponding Author entity
     */
    public Author toEntity(AuthorDTO authorDTO) {
        if (authorDTO == null) {
            return null;
        }
        
        Author author = new Author();
        author.setId(authorDTO.getId());
        author.setName(authorDTO.getName());
        author.setAge(authorDTO.getAge());
        author.setFollowersNumber(authorDTO.getFollowersNumber());
        
        return author;
    }

    /**
     * Updates an existing Author entity with data from an AuthorDTO.
     *
     * @param author the Author entity to update
     * @param authorDTO the AuthorDTO containing the new data
     * @return the updated Author entity
     */
    public Author updateEntityFromDTO(Author author, AuthorDTO authorDTO) {
        if (author == null || authorDTO == null) {
            return author;
        }
        
        if (authorDTO.getName() != null) {
            author.setName(authorDTO.getName());
        }
        
        if (authorDTO.getAge() != null) {
            author.setAge(authorDTO.getAge());
        }
        
        if (authorDTO.getFollowersNumber() != null) {
            author.setFollowersNumber(authorDTO.getFollowersNumber());
        }
        
        return author;
    }
}