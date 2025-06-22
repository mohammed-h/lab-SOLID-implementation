package com.example.booksapi.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String id) {
        super("Book with id " + id + " not found");
    }
}