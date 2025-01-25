package com.coursera.employeemanagement.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/contact")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Value("${spring.mail.username}")
    private String mailUsername;

    private final Map<String, Instant> emailThrottle = new HashMap<>();


    @PostMapping
    public ResponseEntity<String> sendContactEmail(@RequestBody Map<String, String> request) {

        Instant now = Instant.now();
        if (emailThrottle.containsKey(mailUsername) &&
                emailThrottle.get(mailUsername).plusSeconds(60).isAfter(now)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("You have just sent one email. Please wait before sending another one.");
        }

        emailThrottle.put(mailUsername, now);


        String name = request.get("name");
        String from = request.get("email");
        String message = request.get("message");

        // Validate input (optional)
        if (name == null || from == null || message == null) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        // Send from
        String subject = "New Contact Form Submission from " + name;
        String body = "Name: " + name + "\nEmail: " + from + "\nMessage:\n" + message;

        try {
//            emailService.sendEmail(mailUsername, subject, body, from);
            return ResponseEntity.ok("Message sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send message. Please try again later.");
        }
    }
}
