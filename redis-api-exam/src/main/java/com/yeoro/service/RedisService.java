package com.yeoro.service;

import org.springframework.stereotype.Service;

import com.yeoro.domain.RedisCrudRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {

	private final RedisCrudRepository redisCrudRepository;
	
	public Long save(RediscrudSaveRequestDto requestDto) {
		return redisCrudRepository.save(requestDto.toRedisHash()).getId();
	}
	
	public RedisCrudResponseDto get(Long id) {
		RedisCrud redisCrud = redisCrudRepository.findById(id).orElseThrow(
				() -> new IllegalArgumentException("Nothing saved. id = " + id));
		
		return new RedisCrudResponseDto(redisCrud);
	}
}
