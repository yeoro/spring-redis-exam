package com.yeoro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

// Redis ���� ������ ȣ���ϴ� ��Ʈ�ѷ��� ����ϱ� ���� JSON ������ ���־�� API ȣ��� ������ ��Ʈ �� ���ø����̼ǿ��� ���������� JSON ������ ������ �����ϴ�.

@Configuration
public class JsonConfig {

	@Bean
	public ObjectMapper objectMapper() {
		return Jackson2ObjectMapperBuilder.json()
				
				// ��ü�� Serialize ���� �� ��ü�� ��������� �����ϴ� ��� ��Ȱ��ȭ 
				.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
				
				// timestamp �������� �����ϴ� ��� ��Ȱ��ȭ
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				
				// JavaTimeModule�� Ȱ��ȭ�� ObjectMapper�� ������ ��Ʈ ���ø����̼ǿ��� ����� �� �ֵ��� ����
				.modules(new JavaTimeModule())
				.build();
				
	}
}
