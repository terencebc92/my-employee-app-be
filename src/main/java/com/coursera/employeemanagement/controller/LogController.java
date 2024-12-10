package com.coursera.employeemanagement.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/logs")
public class LogController {

    private static final String LOG_DIR = "logs"; // Directory where logs are stored


    // Endpoint to list all log files
    @GetMapping
    public ResponseEntity<List<String>> listLogs() {
        File logDir = new File(LOG_DIR);

        // Check if log directory exists
        if (!logDir.exists() || !logDir.isDirectory()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of("Log directory not found."));
        }

        // List all files in the log directory
        File[] files = logDir.listFiles();
//        File[] files = logDir.listFiles((dir, name) -> name.endsWith(".log"));
        List<String> logFiles = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                logFiles.add(file.getName());
            }
        }

        return ResponseEntity.ok(logFiles);
    }

    // Endpoint to serve a specific log file
    @GetMapping("/{fileName}")
    @ResponseBody
    public ResponseEntity<byte[]> getLogFile(@PathVariable String fileName) {
        Path logFilePath = Paths.get(LOG_DIR, fileName);

        // Check if the requested log file exists
        if (!Files.exists(logFilePath)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("Log file " + fileName + " not found.").getBytes());
        }

        try {
            byte[] fileContent = Files.readAllBytes(logFilePath);

            // Serve the log file as plain text
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error reading log file: " + e.getMessage()).getBytes());
        }
    }
}
