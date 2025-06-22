package com.example.booksapi.service;

import com.example.booksapi.dto.BookMessageDTO;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

@Service
public class BookMessageService {

    private final KafkaTemplate<String, BookMessageDTO> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final JsonFactory jsonFactory;
    private final String topic;
    private final String filePath;

    public BookMessageService(
            KafkaTemplate<String, BookMessageDTO> kafkaTemplate,
            @Value("${kafka.topic.name}") String topic,
            @Value("${message.file.path}") String filePath) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
        this.filePath = filePath;
        this.objectMapper = new ObjectMapper();
        this.jsonFactory = new JsonFactory(objectMapper);
    }

    public void processAndSendMessages() throws IOException {
        final String today = LocalDate.now().toString();

        try (JsonParser jsonParser = jsonFactory.createParser(new File(filePath))) {
            if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Expected content to be an array");
            }

            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                BookMessageDTO message = objectMapper.readValue(jsonParser, BookMessageDTO.class);
                message.setDate(today);
                kafkaTemplate.send(topic, message);
            }
        }
    }
}