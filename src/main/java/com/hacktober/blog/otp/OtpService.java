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
		SecureRandom random = new SecureRandom();
		int otp = 100000 + random.nextInt(900000);
		jedis.setex(email, 300, String.valueOf(otp));
		return String.valueOf(otp);
	}

	public boolean validateOtp(String email, String inputOtp) {
		String storedOtp = jedis.get(email);
		if (storedOtp != null && storedOtp.equals(inputOtp)) {
			jedis.del(email);
			return true;
		}
		return false;
	}
	
	
}
