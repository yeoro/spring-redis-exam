package com.yeoro.controller;

import java.util.Arrays;
import java.util.Set;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yeoro.dto.RedisCrudResponseDto;
import com.yeoro.dto.RedisCrudSaveRequestDto;
import com.yeoro.service.RedisCrudService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RedisController {

	private final RedisCrudService redisCrudService;
	private final StringRedisTemplate redisTemplate;
	
	@GetMapping("/")
	public String ok() {
		return "ok";
	}
	
	@GetMapping("/keys")
	public String keys() {
		Set<String> keys = redisTemplate.opsForSet().members("*");
		assert keys != null;
		
		return Arrays.toString(keys.toArray());
	}
	
	@PostMapping("/save")
	public Long save(@RequestBody RedisCrudSaveRequestDto requestDto) {
		log.info(">>>>>>>>>> [save] redisCrud={}", requestDto);
		
		return redisCrudService.save(requestDto);
	}
	
	@GetMapping("/get/{id}")
	public RedisCrudResponseDto get(@PathVariable("id") Long id) {
		return redisCrudService.get(id);
	}
}