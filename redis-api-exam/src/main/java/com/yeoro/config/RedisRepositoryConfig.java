package com.yeoro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;

/**
 * Redis의 연결을 정의하는 클래스
 * RedisConnectionFactory를 통해 내장 혹은 외부의 Redis 연결
 * RedisTemplate을 통해 RedisConnection에서 넘겨준 byte값을 객체 직렬화
 */
@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisRepositoryConfig {

	private final RedisProperties redisProperties;
	
	// Lettuce
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
	}
	
	@Bean
	public RedisTemplate<?, ?> redisTemplate() {
		RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		
		return redisTemplate;
	}
}
