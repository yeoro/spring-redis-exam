package com.yeoro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


@Configuration
public class SwaggerConfig {
	
	/*
	 * Swagger API ���� ����
	 */
	@Bean
	public Docket api() {
		return new Docket (DocumentationType.SWAGGER_2)
				.apiInfo(this.swaggerInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.yeoro.controller"))
				.paths(PathSelectors.any())
				.build();
	}
	
	/*
	 * Swagger ����
	 */
	private ApiInfo swaggerInfo() {
		return new ApiInfoBuilder()
				.title("Spring Boot with Redis Caching")
				.description("Spring Boot�� Redis�� ����� ������ ȸ�� ���� API ���� ����")
				.version("1.0.0")
				.build();
	}
}