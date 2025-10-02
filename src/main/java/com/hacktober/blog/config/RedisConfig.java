package com.hacktober.blog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.UnifiedJedis;

@Configuration
public class RedisConfig {

    @Value("${redis.host:127.0.0.1}")
    private String host;

    @Value("${redis.port:6379}")
    private int port;

    @Value("${redis.password:}")
    private String password;

    @Value("${redis.ssl:false}")
    private boolean ssl;

    @Value("${redis.timeout-ms:2000}")
    private int timeoutMs;

    @Value("${redis.client-name:hacktoberblog-backend}")
    private String clientName;

    @Bean(destroyMethod = "close")
    public UnifiedJedis unifiedJedis() {
        DefaultJedisClientConfig cfg = DefaultJedisClientConfig.builder()
                .password((password != null && !password.isBlank()) ? password : null)
                .ssl(ssl)
                .connectionTimeoutMillis(timeoutMs)
                .socketTimeoutMillis(timeoutMs)
                .clientName(clientName)
                .build();
        return new UnifiedJedis(new HostAndPort(host, port), cfg);
    }
}
