package com.yeoro.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.yeoro.config.CacheKey;
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
	
	/*
	 * @Cacheable 선언시 해당 메서드가 호출될 때 캐시가 없으면 DB에서 가져와 캐시를 생성하고 데이터를 반환
	 * 캐시가 있는 경우 DB를 거치지 않고 바로 캐시 데이터 반환
	 * value = 저장시 입력할 키값
	 * key = 키 생성시 추가로 덧붙일 파라미터 정보 선언
	 * unless = 메서드 결과가 null이 아닌 경우에만 캐싱하도록 설정하는 옵션
	 */
	@Cacheable(value = CacheKey.REDISCRUD, key = "#id", unless = "#result == null")
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
