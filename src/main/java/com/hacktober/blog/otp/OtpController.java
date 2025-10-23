package com.hacktober.blog.otp;

import com.hacktober.blog.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hacktober.blog.email.EmailService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/otp")
@CrossOrigin("*")
@Tag(name = "One Time Password", description = "Endpoints for generating and validating OTP codes")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService; // reuse your existing email sender

    /**
     * Generate OTP and send it to the provided email.
     */
    @PostMapping("/generate")
    @Operation(summary = "Generate OTP", description = "Create a new OTP code for the provided email and send it via email.")
    public ResponseEntity<ApiResponse<Map<String, Object>>> generateOtp(@RequestParam String email) {
        Map<String, Object> data = new HashMap<>();
        try {
            String otp = otpService.generateOtp(email);
            emailService.sendEmail(email, "Your OTP for Hacktober Blog",
                    "Use the following One-Time Password (OTP) to verify your email address:\n\n" +
                            "OTP: " + otp + "\n\n" +
                            "This OTP is valid for 5 minutes.\n\n" +
                            "If you did not request this, please ignore this email.\n\n" +
                            "Hacktober Blog Team");

            data.put("email", email);
            return ResponseEntity.ok(ApiResponse.success(data, "An OTP has been sent to " + email));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to generate or send OTP. Please try again.",
                            "OTP_GEN_ERR",
                            HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }




    /**
     * Verify OTP for a given email.
     */
    @PostMapping("/verify")
    @Operation(summary = "Verify OTP", description = "Validate that the submitted OTP matches the generated token for the email.")
    public ResponseEntity<ApiResponse<Map<String, Object>>> verifyOtp(@RequestParam String email,
                                                                      @RequestParam String otp) {
        Map<String, Object> data = new HashMap<>();
        data.put("email", email);

        try {
            boolean success = otpService.verifyOtp(email.trim(), otp.trim());
            if (success) {
                return ResponseEntity.ok(ApiResponse.success(data, "OTP verified successfully."));
            } else {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error("Invalid or expired OTP. Please try again.",
                                "OTP_VERIFY_FAIL",
                                HttpStatus.UNAUTHORIZED));
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("OTP verification could not be completed. Please try again.",
                            "OTP_VERIFY_ERR",
                            HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}
