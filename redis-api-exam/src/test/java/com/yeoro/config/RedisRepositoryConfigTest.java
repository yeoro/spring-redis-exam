package com.yeoro.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.RequiredArgsConstructor;


/**
 * 데이터 타입별 테스트를 진행하여 실제 서버에 키-값이 저장되는지 확인
 * 
 * 각 데이터 타입별 직렬화 인터페이스
 * String - opsForValue
 * List - opsForList
 * Set - opsForSet
 * ZSet - opsForZSet
 * Hash - opsForHash
 */
@SpringBootTest
public class RedisRepositoryConfigTest{

	@Autowired
	StringRedisTemplate redisTemplate;
	
	/*
	 * String
	 * 	- GET key
	 */
	@Test
	public void testString() {
		final String key = "testString";
		final ValueOperations<String, String> stringStringValueOperations = redisTemplate.opsForValue();
		
		stringStringValueOperations.set(key, "1");
		final String result_1 = stringStringValueOperations.get(key);
		System.out.println("result_1 = " + result_1);
		
		stringStringValueOperations.increment(key);
		final String result_2 = stringStringValueOperations.get(key);
		System.out.println("result_2 = " + result_2);
	}
	
	/*
	 * List
	 *  - INDEX key index
	 *  - LRANGE key start stop
	 */
	@Test
	public void testList() {
		final String key = "testList";
		final ListOperations<String, String> stringStringListOperations = redisTemplate.opsForList();
		
		stringStringListOperations.rightPushAll(key, "L", "I", "S", "T");
		
		final String character_1 = stringStringListOperations.index(key, 1);
		System.out.println("character_1 = " + character_1);
		
		final Long size = stringStringListOperations.size(key);
		System.out.println("size = " + size);

		final List<String> resultRange = stringStringListOperations.range(key, 0, size);
		
		System.out.println("resultRange = " + Arrays.toString(resultRange.toArray()));
	}
	
	/*
	 * Set
	 * 	- SMEMBERS key
	 */
	@Test
	public void testSet() {
		final String key = "testSet";
		SetOperations<String, String> stringStringSetOperations = redisTemplate.opsForSet();
		
		stringStringSetOperations.add(key, "S");
		stringStringSetOperations.add(key, "E");
		stringStringSetOperations.add(key, "T");
		
		Set<String> test = stringStringSetOperations.members(key);
		
		System.out.println("member = " + Arrays.toString(test.toArray()));
		
		final Long size = stringStringSetOperations.size(key);
		System.out.println("size = " + size);
		
		Cursor<String> cursor = stringStringSetOperations.scan(key, ScanOptions.scanOptions().match("*").count(3).build());
		
		while(cursor.hasNext()) {
			System.out.println("cursor = " + cursor.next());
		}
	}
	
	/*
	 * Sorted Set
	 * 	- ZRANGE key start stop [WITHSCORES]
	 * 	- ZRANGEBYSCORE key min max [WITHSCORES]
	 */
	@Test
	public void testSortedSet() {
		final String key = "testSortedSet";
		ZSetOperations<String, String> stringStringZSetOperations = redisTemplate.opsForZSet();
		
		stringStringZSetOperations.add(key, "Z", 1);
		stringStringZSetOperations.add(key, "S", 5);
		stringStringZSetOperations.add(key, "E", 10);
		stringStringZSetOperations.add(key, "T", 15);
		
		Set<String> range = stringStringZSetOperations.range(key, 0, 5);
		
		System.out.println("range = " + Arrays.toString(range.toArray()));
		
		final Long size = stringStringZSetOperations.size(key);
		System.out.println("size = " + size);
		
		Set<String> scoreRange = stringStringZSetOperations.rangeByScore(key, 0, 13);
		
		System.out.println("range = " + Arrays.toString(scoreRange.toArray()));
	}
	
	/*
	 * Hash
	 * 	- HGET key field
	 */
	@Test
	public void testHash() {
		final String key = "testHash";
		HashOperations<String, Object, Object> stringObjectObjectHashOperations = redisTemplate.opsForHash();
		
		stringObjectObjectHashOperations.put(key, "Hash", "testHash");
		stringObjectObjectHashOperations.put(key, "Hash2", "testHash2");
		stringObjectObjectHashOperations.put(key, "Hash3", "testHash3");
		stringObjectObjectHashOperations.put(key, "Hash4", "testHash4");
		
		Object hash = stringObjectObjectHashOperations.get(key, "Hash");
		System.out.println("hash = " + hash);
		
		Map<Object, Object> entries = stringObjectObjectHashOperations.entries(key);
		System.out.println("entries = " + entries.get("Hash2"));
		
		final Long size = stringObjectObjectHashOperations.size(key);
		System.out.println("size = " + size);
		
	}
}
