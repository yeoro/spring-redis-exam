package com.yeoro.config;

/*
 * 관리 편의성을 위해 캐시 세팅시 사용할 Key(Entity)의 정적 정보를 클래스로 정의
 */
public class CacheKey {
    public static final int DEFAULT_EXPIRE_SEC = 60; // 1 minutes
    public static final String REDISCRUD = "redisCrud";
    public static final int REDISCRUD_EXPIRE_SEC = 60 * 5; // 5 minutes
}
