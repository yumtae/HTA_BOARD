package com.naver.myboard2.service;

import java.util.List;

import com.naver.myboard2.domain.Board;

public interface BoardService {
	
	//���� ���� ���ϱ�
	public int getListCount();
	
	//�� ��� ���ϱ�
	public List<Board> getBoardList(int page , int limit);
	
	//�� ���� ����
	public Board getDetail(int num);
	
	//�� �亯 
	public int boardReply(Board board);
	
	//�� �亯 
	public int boardReplyUpdate(Board board);
	
	//�� ����
	public int boardModify(Board modifyboard);
	
	//�� ����
	public int boardDelete(int num);
	
	
	//��ȸ�� ������Ʈ
	public int setReadCountUpdate(int num);
	
	//�۾������� Ȯ��
	public boolean isBoardWriter(int num, String pass);
	
	//�� ����ϱ�
	public void insertBoard(Board board);

	//���ϻ���
	public List<String> getDeleteFileList();
	//���ϻ���
	public void deleteFileList(String filename);
	
}
