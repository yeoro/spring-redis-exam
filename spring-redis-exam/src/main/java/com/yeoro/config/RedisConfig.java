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
	
	// Redis ����
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
	
	// �ؽ�Ʈ ����
	@Bean
	public StringRedisTemplate stringRedisTemplate(@Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		
		return template;
	}
	
	// ��ü ����
	@Bean
	public RedisTemplate<String, byte[]> messagePackRedisTemplate(@Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory){
		RedisTemplate<String, byte[]> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setEnableDefaultSerializer(false);
		
		return template;
	}
	
	// ��ü�� �����̳� ��ȸ �� ����ȭ(Serialize)
	@Bean
	public ObjectMapper messagePackObjectMapper() {
		return new ObjectMapper(new MessagePackFactory())
				.registerModule(new JavaTimeModule()) // ��¥ ����
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ��¥�� timestamp �������� �����ϴ� ��� ��Ȱ��ȭ
	}
}
