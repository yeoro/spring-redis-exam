package com.yeoro.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.yeoro.domain.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserDAO {
	
	private static final String USER_KEY = "USERS:${USERNAME}";
	private final RedisConnectionFactory redisConnectionFactory;
	
	// 객체를 저장하거나 가져오는 역할
	private final RedisTemplate<String, byte[]> messagePackRedisTemplate;
	
	// 객체를 Serialize하여 byte로 저장하거나 조회하는 역할
	private final ObjectMapper messagePackObjectMapper;
	
	// 사용자 조회
	public User getUser(String username) throws IOException {
		String key = StringSubstitutor.replace(USER_KEY, ImmutableMap.of("USERNAME", username));
		
		byte[] message = messagePackRedisTemplate.opsForValue().get(key);
		
		if(message == null) {
			return null;
		}
		
		return messagePackObjectMapper.readValue(message, User.class);
	}
	
	// 사용자 등록
	public void setUser(User user) throws JsonProcessingException {
		String key = StringSubstitutor.replace(USER_KEY, ImmutableMap.of("USERNAME", user.getUsername()));
		
		byte[] message = messagePackObjectMapper.writeValueAsBytes(user);
		
		messagePackRedisTemplate.opsForValue().set(key, message, 1, TimeUnit.HOURS);
	}
	
	// 사용자 삭제
	public void deleteUser(String username) {
		String key = StringSubstitutor.replace(USER_KEY, ImmutableMap.of("USERNAME", username));
		
		messagePackRedisTemplate.delete(key);
	}
	
	/** 
	 * 전체 사용자 조회
	 * 레디스 전체를 가져오는 keys 명령어가 있지만, 레디스 키가 많은 상황에서 사용하면 결과를 응답할 때 까지 퍼포먼스가 심각하게 떨어짐
	 * 레디스 서버에 부하를 주지 않고 모든 키를 가져오는 scan 명령어 사용
	 * scan은 커서 기반의 iterlator로, 맨 처음 커서를 0으로 두고 요청하면 데이터 리스트와 함께 다음 커서를 반환하는 식으로 순차적으로 호출하여 모든 키를 가져옴
	 */
	public List<String> getAllUsers() {
		String key = StringSubstitutor.replace(USER_KEY, ImmutableMap.of("USERNAME", "*"));
		
		RedisConnection redisConnection = redisConnectionFactory.getConnection();
		ScanOptions options = ScanOptions.scanOptions().count(50).match(key).build();
		
		List<String> users = new ArrayList<>();
		Cursor<byte[]> cursor = redisConnection.scan(options);
		
		while(cursor.hasNext()) {
			String user = StringUtils.replace(new String(cursor.next()), "USERS:", "");
			
			users.add(user);
		}
		
		return users;
	}
}
