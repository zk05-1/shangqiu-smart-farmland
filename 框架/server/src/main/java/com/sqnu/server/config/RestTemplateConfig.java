package com.sqnu.server.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * RestTemplate配置类
 * <p>
 * 用于配置HTTP客户端，支持调用外部API（如天气API）。
 * </p>
 *
 * @author sqnu
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 创建RestTemplate Bean
     *
     * @param builder RestTemplateBuilder
     * @return RestTemplate实例
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(30))
                .build();
    }
}