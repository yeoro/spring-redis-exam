package com.yeoro.config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.embedded.RedisServer;

/**
 * 외부가 아닌 내장 Redis 서버로 환경을 구성할 경우 작성
 * Embedded Redis : H2와 같은 내장 Redis 데몬
 * 설정 파일이 local일 때만 작동하도록 @Profile 어노테이션 추가
 */
@Profile("local")
@Configuration
public class EmbeddedRedisConfig {

	@Value("${spring.redis.port}")
	private int port;
	
	private RedisServer redisServer;
	
	@PostConstruct
	public void redisServer() {
		redisServer = new RedisServer(port);
		redisServer.start();
	}
	
	@PreDestroy
	public void stopRedis() {
		if(redisServer != null) {
			redisServer.stop();
		}
	}
}
