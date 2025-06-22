package com.example.booksapi.service.impl;

import com.example.booksapi.service.ExternalBookService;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * Implementation of ExternalBookService for the OpenLibrary API.
 */
@Service
public class ExternalBookServiceImpl implements ExternalBookService {

    private static final Logger logger = LoggerFactory.getLogger(ExternalBookServiceImpl.class);
    private final WebClient webClient;

    public ExternalBookServiceImpl(@Value("${api.openlibrary.url}") String openLibraryUrl, WebClient.Builder webClientBuilder) {
            this.webClient = webClientBuilder
                .baseUrl(openLibraryUrl)
                .build();
    }


    @Override
    public Mono<JsonNode> findBookByIsbn(String isbn) {
        logger.info("Searching for book with ISBN: {}", isbn);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("bibkeys", "ISBN:" + isbn)
                        .queryParam("format", "json")
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(response -> {
                    JsonNode bookNode = response.get("ISBN:" + isbn);
                    if (bookNode != null && !bookNode.isEmpty()) {
                        logger.info("Found book with ISBN: {}", isbn);
                        return bookNode;
                    }
                    logger.info("No book found with ISBN: {}", isbn);
                    return null;
                })
                .onErrorResume(WebClientResponseException.class, e -> {
                    logger.error("Error fetching book with ISBN {}: {} - {}", 
                            isbn, e.getStatusCode(), e.getMessage());
                    return Mono.empty();
                })
                .onErrorResume(Exception.class, e -> {
                    logger.error("Unexpected error fetching book with ISBN {}: {}", 
                            isbn, e.getMessage());
                    return Mono.empty();
                });
    }
}