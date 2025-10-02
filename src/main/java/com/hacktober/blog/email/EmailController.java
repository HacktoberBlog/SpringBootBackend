package com.hacktober.blog.email;

import com.hacktober.blog.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/mail")
@CrossOrigin("*")
@Tag(name = "Email", description = "Endpoints for sending transactional emails")
public class EmailController {
	
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    @Operation(summary = "Send email", description = "Dispatch an email message using the configured SMTP provider.")
    public ResponseEntity<ApiResponse<String>> sendMail(@Valid @RequestBody EmailRequest request) {
        emailService.sendEmail(request.getTo(), request.getSubject(), request.getBody());
        String message = "Email sent successfully to " + request.getTo();
        return ResponseEntity.ok(ApiResponse.success(message, "Email sent successfully"));
    }
}
