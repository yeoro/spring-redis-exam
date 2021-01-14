package com.yeoro.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;

/**
 * Redis�� ������ �����ϴ� Ŭ����
 * RedisConnectionFactory�� ���� ���� Ȥ�� �ܺ��� Redis ����
 * RedisTemplate�� ���� RedisConnection���� �Ѱ��� byte���� ��ü ����ȭ
 * @EnableCaching�� �����Ͽ� Redis ĳ�� ��� Ȱ��ȭ
 */
@Configuration
@EnableRedisRepositories
@EnableCaching
@RequiredArgsConstructor
public class RedisConfig {

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
	
	// Cache
	@Bean(name = "cacheManager")
	public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		RedisCacheConfiguration configuration = RedisCacheConfiguration
				.defaultCacheConfig()
				
				// null value�� ĳ�� ����
				.disableCachingNullValues()
				
				//ĳ���� �⺻ ��ȿ�ð� ����
				.entryTtl(Duration.ofSeconds(CacheKey.DEFAULT_EXPIRE_SEC)) 
				.computePrefixWith(CacheKeyPrefix.simple())
				
				// Redis ĳ�� �������� ���� ����� StringSerializer�� ����
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
		
		// Cache Ű �� default ��ȿ�ð� ����
		Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
		cacheConfigurations.put(CacheKey.REDISCRUD, RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofSeconds(CacheKey.REDISCRUD_EXPIRE_SEC)));
		
		return RedisCacheManager.RedisCacheManagerBuilder
				.fromConnectionFactory(connectionFactory)
				.cacheDefaults(configuration)
				.withInitialCacheConfigurations(cacheConfigurations)
				.build();
	}
}
