package com.hq.minio.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

@Data
@Configuration
@ConfigurationProperties(prefix = "encoder.crypt")
public class PasswordConfig {
    /**
     * 加密强度
     */
    private int strength;
    /**
     * 干扰因子
     */
    private String secret;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        //对干扰因子加密
        SecureRandom secureRandom = new SecureRandom(secret.getBytes());
        //对密码加密
        return new BCryptPasswordEncoder(strength, secureRandom);
    }
}
