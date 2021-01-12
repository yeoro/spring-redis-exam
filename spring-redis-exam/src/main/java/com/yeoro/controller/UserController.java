package com.yeoro.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;
import com.yeoro.domain.User;
import com.yeoro.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/spring-redis")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	// 전체 사용자 조회
	@GetMapping("/users")
	public ResponseEntity<?> getAllUsers() {
		List<String> users = userService.getUsernameList();
		
		return new ResponseEntity<>(ImmutableMap.of("users", users), HttpStatus.OK);
	}
	
	// 사용자 조회
	@GetMapping("/users/{username}")
	public ResponseEntity<?> getUser(@PathVariable("username") String username) throws IOException {
		User user = userService.getUser(username);
		
		if(user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	// 사용자 등록
	@PostMapping("/users")
	public ResponseEntity<?> registerUser(@RequestBody RegisterUserRequest request) {
		User user = userService.registerUser(request.getUsername());
		
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	// 사용자 삭제
	@DeleteMapping("/users/{username}")
	public ResponseEntity<?> deleteUser(@PathVariable("username") String username) {
		userService.deleteUser(username);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
