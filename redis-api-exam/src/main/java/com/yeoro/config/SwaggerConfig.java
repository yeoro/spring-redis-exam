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
	 * Swagger API 문서 생성
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
	 * Swagger 정보
	 */
	private ApiInfo swaggerInfo() {
		return new ApiInfoBuilder()
				.title("Spring Boot with Redis Caching")
				.description("Spring Boot와 Redis를 사용한 간단한 회원 관리 API 구현 예제")
				.version("1.0.0")
				.build();
	}
}