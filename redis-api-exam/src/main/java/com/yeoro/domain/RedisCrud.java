package com.yeoro.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@RedisHash("redisCrud")
public class RedisCrud implements Serializable{

	@Id
	private Long id;
	private String description;
	private LocalDateTime updatedAt;
	
	public void update(String description, LocalDateTime updatedAt) {
		this.description = description;
		this.updatedAt = updatedAt;
	}
}
