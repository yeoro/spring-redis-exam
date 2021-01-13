package com.yeoro.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * application.properties�� �Է��� Redis ������ ���� ��ü
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
