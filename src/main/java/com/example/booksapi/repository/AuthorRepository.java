package com.example.booksapi.repository;

import com.example.booksapi.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Author entity providing CRUD operations.
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {}