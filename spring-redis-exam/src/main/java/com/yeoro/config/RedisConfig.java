package com.yeoro.config;

import java.time.Duration;

import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
	
	@Value("${redis.hostname}")
	private final String HOSTNAME;
	
	@Value("${redis.port}")
	private final int PORT;
	
	@Value("${redis.database}")
	private final int DATABASE;
	
	@Value("${redis.password}")
	private final String PASSWORD;
	
	@Value("${redis.timeout}")
	private final Long TIMEOUT;
	
	// Redis 접속
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
		config.setHostName(HOSTNAME);
		config.setPort(PORT);
		config.setDatabase(DATABASE);
		config.setPassword(PASSWORD);
		
		LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
				.commandTimeout(Duration.ofMillis(TIMEOUT))
				.build();
		
		return new LettuceConnectionFactory(config, clientConfig);
	}
	
	// 텍스트 저장
	@Bean
	public StringRedisTemplate stringRedisTemplate(@Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		
		return template;
	}
	
	// 객체 저장
	@Bean
	public RedisTemplate<String, byte[]> messagePackRedisTemplate(@Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory){
		RedisTemplate<String, byte[]> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setEnableDefaultSerializer(false);
		
		return template;
	}
	
	// 객체의 저장이나 조회 시 직렬화(Serialize)
	@Bean
	public ObjectMapper messagePackObjectMapper() {
		return new ObjectMapper(new MessagePackFactory())
				.registerModule(new JavaTimeModule()) // 날짜 저장
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 날짜를 timestamp 형식으로 저장하는 기능 비활성화
	}
}
