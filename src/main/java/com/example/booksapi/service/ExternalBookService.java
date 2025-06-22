package com.example.booksapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Mono;

public interface ExternalBookService {
    Mono<JsonNode> findBookByIsbn(String isbn);
}