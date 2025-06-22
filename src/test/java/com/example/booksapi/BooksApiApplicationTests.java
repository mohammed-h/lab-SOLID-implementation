package com.example.booksapi;

import com.example.booksapi.dto.BookRequestDTO;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.profiles.active=test"
)
class BooksApiApplicationTests {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldUpdateAnExistingBook() {
        BookRequestDTO bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setTitle("Scrum: The Art of Doing Twice the Work in Half the Time");
        bookRequestDTO.setAuthorId(1L);
        bookRequestDTO.setPublicationDate(LocalDate.of(2014, 9, 30));
        bookRequestDTO.setType("Software Development");

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", "aedz-151-ftyh-554");

        HttpEntity<BookRequestDTO> request = new HttpEntity<>(bookRequestDTO,headers);

        ResponseEntity<Void> response = restTemplate
                .exchange("/api/books/2", HttpMethod.PUT, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        HttpEntity<Void> getRequest = new HttpEntity<>(headers);
        ResponseEntity<String> getResponse = restTemplate.exchange(
                "/api/books/2",
                HttpMethod.GET,
                getRequest,
                String.class
        );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
        Number id = documentContext.read("$.id");
        String type = documentContext.read("$.type");
        assertThat(id).isEqualTo(2);
        assertThat(type).isEqualTo("Software Development");
    }

}
