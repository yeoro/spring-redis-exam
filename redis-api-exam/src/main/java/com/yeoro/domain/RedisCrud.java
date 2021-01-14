package com.yeoro.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Data;

/*
 * Redis에 객체를 저장하면 내부적으로 직렬화되어 저장되는데, 모델 class에 Serializable을 선언해주지 않으면 오류가 발생할 수 있음!
 */
@Data
@Builder
@RedisHash("redisCrud")
public class RedisCrud implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	private String description;
	private LocalDateTime updatedAt;
	
	public void update(String description, LocalDateTime updatedAt) {
		this.description = description;
		this.updatedAt = updatedAt;
	}
}
