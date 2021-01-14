package com.yeoro.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.yeoro.domain.RedisCrud;
import com.yeoro.domain.RedisCrudRepository;
import com.yeoro.dto.RedisCrudResponseDto;
import com.yeoro.dto.RedisCrudSaveRequestDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisCrudService {

	private final RedisCrudRepository redisCrudRepository;
	private final StringRedisTemplate redisTemplate;
	
	public Long save(RedisCrudSaveRequestDto requestDto) {
		return redisCrudRepository.save(requestDto.toRedisHash()).getId();
	}
	
	public RedisCrudResponseDto get(Long id) {
		RedisCrud redisCrud = redisCrudRepository.findById(id).orElseThrow(
				() -> new IllegalArgumentException("Nothing saved. id = " + id));
		
		return new RedisCrudResponseDto(redisCrud);
	}
	
	public List<String> getAll() {
		RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
		ScanOptions options = ScanOptions.scanOptions().match("*").count(10).build();
		
		List<String> users = new ArrayList<>();
		
		Cursor<byte[]> cursor = redisConnection.scan(options);
		
		while(cursor.hasNext()) {
			users.add(new String(cursor.next()));
		}
		
		redisConnection.close();
		return users;
	}
	
	public String delete(Long id) {
		RedisCrud redisCrud = redisCrudRepository.findById(id).orElseThrow(
				() -> new IllegalArgumentException("Nothing saved. id = " + id));
		redisCrudRepository.delete(redisCrud);
		
		return "ok";
	}
}
