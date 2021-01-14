package com.yeoro.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.yeoro.domain.RedisCrud;

import lombok.Data;

@Data
public class RedisCrudResponseDto implements Serializable{

	private Long id;
	private String description;
	private LocalDateTime updatedAt;
	
	public RedisCrudResponseDto(RedisCrud redisHash) {
		this.id = redisHash.getId();
		this.description = redisHash.getDescription();
		this.updatedAt = redisHash.getUpdatedAt();
	}
}
