package com.yeoro.dto;

import java.time.LocalDateTime;

import com.yeoro.domain.RedisCrud;

import lombok.Data;

@Data
public class RedisCrudResponseDto {

	private Long id;
	private String description;
	private LocalDateTime updatedAt;
	
	public RedisCrudResponseDto(RedisCrud redisHash) {
		this.id = redisHash.getId();
		this.description = redisHash.getDescription();
		this.updatedAt = redisHash.getUpdatedAt();
	}
}
