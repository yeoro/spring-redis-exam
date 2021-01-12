package com.yeoro.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.yeoro.dao.UserDAO;
import com.yeoro.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserDAO userDao;
	
	// 사용자 등록
	public User registerUser(String username) throws IOException {
		User user = new User();
		user.setUsername(username);
		user.setCreateAt(LocalDateTime.now());
		
		userDao.setUser(user);
		
		return userDao.getUser(username);
	}
	
	// 사용자 조회
	public User getUser(String username) throws IOException{
		return userDao.getUser(username);
	}
	
	// 전체 사용자 조회
	public List<String> getUsernameList() {
		return userDao.getAllUsers();
	}
	
	// 사용자 삭제
	public void deleteUser(String username) {
		userDao.deleteUser(username);
	}
}
