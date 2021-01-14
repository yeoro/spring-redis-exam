package com.yeoro.service;

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
	
	public Long save(RedisCrudSaveRequestDto requestDto) {
		return redisCrudRepository.save(requestDto.toRedisHash()).getId();
	}
	
	public RedisCrudResponseDto get(Long id) {
		RedisCrud redisCrud = redisCrudRepository.findById(id).orElseThrow(
				() -> new IllegalArgumentException("Nothing saved. id = " + id));
		
		return new RedisCrudResponseDto(redisCrud);
	}
	
	public String delete(Long id) {
		RedisCrud redisCrud = redisCrudRepository.findById(id).orElseThrow(
				() -> new IllegalArgumentException("Nothing saved. id = " + id));
		redisCrudRepository.delete(redisCrud);
		
		return "ok";
	}
}
