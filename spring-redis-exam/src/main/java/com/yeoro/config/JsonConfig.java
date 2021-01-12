package com.yeoro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// Redis 서비스 로직을 호출하는 컨트롤러를 사용하기 위해 JSON 설정을 해주어야 API 호출시 스프링 부트 웹 어플리케이션에서 정상적으로 JSON 형식의 응답이 가능하다.

@Configuration
public class JsonConfig {

	@Bean
	public ObjectMapper objectMapper() {
		return Jackson2ObjectMapperBuilder.json()
				
				// 객체를 Serialize 했을 때 객체가 비어있으면 실패하는 기능 비활성화 
				.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
				
				// timestamp 형식으로 저장하는 기능 비활성화
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				
				// JavaTimeModule을 활성화한 ObjectMapper를 스프링 부트 어플리케이션에서 사용할 수 있도록 설정
				.modules(new JavaTimeModule())
				.build();
				
	}
}
