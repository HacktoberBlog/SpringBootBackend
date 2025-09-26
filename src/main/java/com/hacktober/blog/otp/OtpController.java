package com.hacktober.blog.otp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hacktober.blog.email.EmailService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService; // reuse your existing email sender

    /**
     * Generate OTP and send it to the provided email.
     */
    @PostMapping("/generate")
    public Map<String, Object> generateOtp(@RequestParam String email) {
        String otp = otpService.generateOtp(email);
        emailService.sendEmail(email, "Your OTP Code", "Your OTP code is: " + otp);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "OTP sent to your email.");
        return response;
    }

    /**
     * Verify OTP for a given email.
     */
    @PostMapping("/verify")
    public Map<String, Object> verifyOtp(@RequestParam String email,
                                         @RequestParam String otp) {
        boolean isValid = otpService.validateOtp(email, otp);
        Map<String, Object> response = new HashMap<>();
        if (isValid) {
            response.put("message", "OTP verified successfully.");
        } else {
            response.put("message", "Invalid OTP.");
        }
        return response;
    }
}
