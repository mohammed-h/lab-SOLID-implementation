package com.example.booksapi.service;

import com.example.booksapi.dto.BookMessageDTO;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.FileWriter;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BookMessageServiceTest {

    @Test
    void shouldReadEnrichAndSendMessages() throws Exception {
        String path = "src/test/resources/test-messages.json";
        String json = """
                [
                  { "bookId": 1, "title": "Clean Code", "author": "Robert Martin" }
                ]
                """;
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(json);
        }

        KafkaTemplate<String, BookMessageDTO> kafkaTemplate = mock(KafkaTemplate.class);
        BookMessageService service = new BookMessageService(kafkaTemplate, "book-topic", path);

        service.processAndSendMessages();

        ArgumentCaptor<BookMessageDTO> captor = ArgumentCaptor.forClass(BookMessageDTO.class);
        verify(kafkaTemplate).send(eq("book-topic"), captor.capture());

        BookMessageDTO sentMessage = captor.getValue();
        assertThat(sentMessage.getBookId()).isEqualTo(1);
        assertThat(sentMessage.getTitle()).isEqualTo("Clean Code");
        assertThat(sentMessage.getAuthor()).isEqualTo("Robert Martin");
        assertThat(sentMessage.getDate()).isEqualTo(LocalDate.now().toString());
    }
}