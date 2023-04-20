package com.naver.myboard2.service;

import java.util.List;

import com.naver.myboard2.domain.Comment;

public interface CommentService {
	
	
	//���� ���� ���ϱ�
	public int getListCount(int board_num);
	
	//��� ��� ��������
	public List<Comment> getCommentList(int board_num, int page);
	
	
	//��� ����
	public int commentInsert(Comment c);
	
	//��� ����
	public int commentDelete(int num);
	
	//��� ����
	public int commentUpdate(Comment co);
	
	
	
}
