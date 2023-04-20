package com.naver.security;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.naver.myboard2.domain.Member;
/*
 	1. UserDetailsService �������̽��� DB���� ���� ������ �ҷ����� loadUserByUsername()�� �����մϴ�
  		�̸� �����ϴ� Ŭ������ DB���� ������ ������ �����ͼ� UserDetails Ÿ������ ������ �ִ� �۾��� �մϴ�.
  		
  	2. UserDetails�� �������̽��� Security���� ������� ������ ��� �������̽��Դϴ�.
  	
  	3. UserDeails �������̽��� ������ Ŭ������ ������ ������� ������ ����ڰ� ���� ������ ������ ó���ؼ� ��ȯ�ϰ� �˴ϴ�.
  			��) UserDetails user = new User(username, user.getPassword(),roles);
 * */
import com.naver.myboard2.mybatis.mapper.MemberMapper;

//@Service
public class CustomUserDetailsService implements UserDetailsService{
	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	
	@Autowired
	private MemberMapper dao;
	
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		logger.info("username�� �α��� �� �Է��� �� : "+username);
		Member users= dao.isId(username);
		
		if(users==null){
			logger.info("username " + username + " not found");
			throw new UsernameNotFoundException("username " + username + " not found");
		}
		
		Collection<SimpleGrantedAuthority> roles = new ArrayList<SimpleGrantedAuthority>();
		
		roles.add(new SimpleGrantedAuthority(users.getAuth()));
		
		UserDetails user = new User(username, users.getPassword(),roles);
		
		return user;
	}
		
	
}
