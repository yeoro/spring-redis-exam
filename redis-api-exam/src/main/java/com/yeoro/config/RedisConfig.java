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
 * Redis의 연결을 정의하는 클래스
 * RedisConnectionFactory를 통해 내장 혹은 외부의 Redis 연결
 * RedisTemplate을 통해 RedisConnection에서 넘겨준 byte값을 객체 직렬화
 * @EnableCaching을 선언하여 Redis 캐시 사용 활성화
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
				
				// null value는 캐싱 안함
				.disableCachingNullValues()
				
				//캐시의 기본 유효시간 설정
				.entryTtl(Duration.ofSeconds(CacheKey.DEFAULT_EXPIRE_SEC)) 
				.computePrefixWith(CacheKeyPrefix.simple())
				
				// Redis 캐시 데이터의 저장 방식을 StringSerializer로 지정
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
		
		// Cache 키 별 default 유효시간 설정
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
