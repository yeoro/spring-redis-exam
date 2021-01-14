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
 * Redis�� ������ �����ϴ� Ŭ����
 * RedisConnectionFactory�� ���� ���� Ȥ�� �ܺ��� Redis ����
 * RedisTemplate�� ���� RedisConnection���� �Ѱ��� byte���� ��ü ����ȭ
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
