package com.naver.myboard2.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.naver.myboard2.domain.Member;
import com.naver.myboard2.mybatis.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService {

	private MemberMapper dao;
	private PasswordEncoder passwordEncoder;
	@Autowired
	public MemberServiceImpl(MemberMapper dao, PasswordEncoder passwordEncoder) {
		this.dao =dao;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public int isId(String id, String password) {
		Member rmember = dao.isId(id);
		int result= -1;
			if(rmember !=null) {
				if( passwordEncoder.matches(password,rmember.getPassword()) ) {
					result = 1;
					
				}else {
					result=0;
				}
			}
		return result;
		
	}

	@Override
	public int insert(Member m) {
		return dao.insert(m);
	}

	@Override
	public int isId(String id) {
		Member rmember = dao.isId(id);
		return (rmember==null)? -1 : 1; // -1�� �������� �ʴ°��
	}

	@Override
	public Member member_info(String id) {
		return dao.isId(id);
	}

	@Override
	public void delete(String id) {
		 dao.delete(id);
	}

	@Override
	public int update(Member m) {
		return dao.update(m);
	}

	@Override
	public List<Member> getSearchList(int index, String search_word, int page, int limit) {
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		if( index != -1) {
			
			String[] search_field = new String[] {"id","name","age","gender"};
			map.put("search_word", "%" +search_word+ "%");
			map.put("search_field",  search_field[index]  );
			
		}
		int startrow = (page-1) * limit +1;
		int endrow = startrow + limit -1;
		map.put("start", startrow);
		map.put("end", endrow);
		return dao.getSearchList(map);
	}

	@Override
	public int getSearchListCount(int index, String search_word) {
		
		HashMap<String, String> map = new HashMap<String, String>();
		
		if( index != -1) {
			
			String[] search_field = new String[] {"id","name","age","gender"};
			
			map.put("search_word", "%" +search_word+ "%");
			map.put("search_field",  search_field[index]  );
			
		}
			return dao.getSearchListCount(map);
		
	
	}
	
	
	
	
}
