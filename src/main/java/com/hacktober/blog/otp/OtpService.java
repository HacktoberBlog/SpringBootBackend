package com.hacktober.blog.otp;

import org.springframework.stereotype.Service;

import redis.clients.jedis.UnifiedJedis;

import java.security.SecureRandom;
import java.time.Duration;

@Service
public class OtpService {

	private final UnifiedJedis jedis;
	private static final SecureRandom RNG = new SecureRandom();

	private static final int OTP_TTL_SEC = (int) Duration.ofMinutes(10).toSeconds();
	private static final int MAX_REQUESTS_PER_HOUR = 5;



	public OtpService(UnifiedJedis jedis) {
		this.jedis = jedis;
	}

	private static String otpKey(String email) {
		return "otp:reset:" + email.toLowerCase();
	}

	private static String countKey(String email) {
		return "otp:reset:count:" + email.toLowerCase();
	}

	/**
	 * Generates a 6-digit OTP, stores it with TTL, and returns it.
	 * Throws IllegalStateException if caller exceeded hourly request limit.
	 */
	public String generateOtp(String email) {
		// Simple per-email rate limit
		String cKey = countKey(email);
		Long count = jedis.incr(cKey);
		if (count != null && count == 1) {
			// first request in this window -> start 1h expiry
			jedis.expire(cKey, 3600);
		}
		if (count != null && count > MAX_REQUESTS_PER_HOUR) {
			throw new IllegalStateException("Too many OTP requests. Try again later.");
		}

		String code = String.format("%06d", RNG.nextInt(1_000_000)); // 000000–999999
		// SET with expiry (overwrite any previous pending OTP)
		jedis.setex(otpKey(email), OTP_TTL_SEC, code);
		return code;
	}

	/**
	 * Validates the OTP and consumes it (one-time). Returns true if valid.
	 */
	public boolean verifyOtp(String email, String inputOtp) {
		String key = otpKey(email);
		String expected = jedis.get(key);
		if (expected == null) {
			return false; // expired or never issued
		}
		boolean ok = expected.equals(inputOtp);
		if (ok) {
			// one-time use: delete after successful validation
			jedis.del(key);
		}
		return ok;
	}
	
	
}
