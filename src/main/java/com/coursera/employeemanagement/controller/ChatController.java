package com.coursera.employeemanagement.controller;

import com.coursera.employeemanagement.dto.ChatDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/chat")
@Slf4j
public class ChatController {

    private ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    @Value("${api.modelUrl}")
    private String modelApiUrl;

    @PostMapping(value = "/generate", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chat(@RequestBody ChatDto chatDto) {
        log.info("{}",chatDto.isStream());
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        CompletableFuture.runAsync(() -> {
            try {
                String requestBody = objectMapper.writeValueAsString(chatDto);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(modelApiUrl))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                HttpResponse<java.io.InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.body()));

                String line;
                while ((line = reader.readLine()) != null) {
                    log.info("Received chunk: {}", line); // Log the chunk received from the LLM
                    emitter.send(line + "\n");
                }

                emitter.complete();
            } catch (Exception e) {
                try {
                    log.info("Some error occurred:", e.getMessage());
                    emitter.send("Error processing request\n");
                } catch (Exception ignored) {
                    log.info("ignored error");
                }
                emitter.completeWithError(e);
            }
        });

        log.info("End request");
        return emitter;
    }


}

