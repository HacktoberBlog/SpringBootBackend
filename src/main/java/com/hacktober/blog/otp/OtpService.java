package com.hacktober.blog.otp;

import org.springframework.stereotype.Service;

import redis.clients.jedis.UnifiedJedis;

import java.security.SecureRandom;

@Service
public class OtpService {

	private final UnifiedJedis jedis;

	public OtpService(UnifiedJedis jedis) {
		this.jedis = jedis;
	}

	public String generateOtp(String email) {
		// Generate a 6 digit OTP using Java library e.g. SecureRandom
		// Set the key as email and value as OTP, with expiration time of 5-6 minutes using jedis object

		return null;
	}

	public boolean verifyOtp(String email, String inputOtp) {
		// Verify OTP function
		// Once verified, delete the key value pair
		return false;
	}
	
	
}
