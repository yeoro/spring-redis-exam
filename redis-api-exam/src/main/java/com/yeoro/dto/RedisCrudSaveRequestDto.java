package com.yeoro.dto;

import java.time.LocalDateTime;

import com.yeoro.domain.RedisCrud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedisCrudSaveRequestDto {
	
	private Long id;
	private String description;
	private LocalDateTime updatedAt;
	
	public RedisCrud toRedisHash() {
		return RedisCrud.builder()
				.id(id)
				.description(description)
				.updatedAt(updatedAt)
				.build();
	}
}
