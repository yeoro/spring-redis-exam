package com.yeoro.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * application.properties에 입력한 Redis 정보에 대한 객체
 */
@Component
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedisProperties {

	private String host;
	private int port;
	private int database;
	private String password;
}
