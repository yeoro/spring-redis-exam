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
	
	// ��ü�� �����ϰų� �������� ����
	private final RedisTemplate<String, byte[]> messagePackRedisTemplate;
	
	// ��ü�� Serialize�Ͽ� byte�� �����ϰų� ��ȸ�ϴ� ����
	private final ObjectMapper messagePackObjectMapper;
	
	// ����� ��ȸ
	public User getUser(String username) throws IOException {
		String key = StringSubstitutor.replace(USER_KEY, ImmutableMap.of("USERNAME", username));
		
		byte[] message = messagePackRedisTemplate.opsForValue().get(key);
		
		if(message == null) {
			return null;
		}
		
		return messagePackObjectMapper.readValue(message, User.class);
	}
	
	// ����� ���
	public void setUser(User user) throws JsonProcessingException {
		String key = StringSubstitutor.replace(USER_KEY, ImmutableMap.of("USERNAME", user.getUsername()));
		
		byte[] message = messagePackObjectMapper.writeValueAsBytes(user);
		
		messagePackRedisTemplate.opsForValue().set(key, message, 1, TimeUnit.HOURS);
	}
	
	// ����� ����
	public void deleteUser(String username) {
		String key = StringSubstitutor.replace(USER_KEY, ImmutableMap.of("USERNAME", username));
		
		messagePackRedisTemplate.delete(key);
	}
	
	/** 
	 * ��ü ����� ��ȸ
	 * ���� ��ü�� �������� keys ��ɾ ������, ���� Ű�� ���� ��Ȳ���� ����ϸ� ����� ������ �� ���� �����ս��� �ɰ��ϰ� ������
	 * ���� ������ ���ϸ� ���� �ʰ� ��� Ű�� �������� scan ��ɾ� ���
	 * scan�� Ŀ�� ����� iterlator��, �� ó�� Ŀ���� 0���� �ΰ� ��û�ϸ� ������ ����Ʈ�� �Բ� ���� Ŀ���� ��ȯ�ϴ� ������ ���������� ȣ���Ͽ� ��� Ű�� ������
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
