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
	
	// ����� ���
	public User registerUser(String username) throws IOException {
		User user = new User();
		user.setUsername(username);
		user.setCreateAt(LocalDateTime.now());
		
		userDao.setUser(user);
		
		return userDao.getUser(username);
	}
	
	// ����� ��ȸ
	public User getUser(String username) throws IOException{
		return userDao.getUser(username);
	}
	
	// ��ü ����� ��ȸ
	public List<String> getUsernameList() {
		return userDao.getAllUsers();
	}
	
	// ����� ����
	public void deleteUser(String username) {
		userDao.deleteUser(username);
	}
}
