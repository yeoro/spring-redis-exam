package com.yeoro.config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.embedded.RedisServer;

/**
 * �ܺΰ� �ƴ� ���� Redis ������ ȯ���� ������ ��� �ۼ�
 * Embedded Redis : H2�� ���� ���� Redis ����
 * ���� ������ local�� ���� �۵��ϵ��� @Profile ������̼� �߰�
 */
@Slf4j
@Profile("local")
@Configuration
@RequiredArgsConstructor
public class EmbeddedRedisConfig {

	private final RedisProperties redisProperties;
	private RedisServer redisServer;
	
	@PostConstruct
	public void redisServer() {
		redisServer = new RedisServer(redisProperties.getPort());
		redisServer.start();
	}
	
	@PreDestroy
	public void stopRedis() {
		if(redisServer != null) {
			redisServer.stop();
		}
	}
}
