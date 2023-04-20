package com.naver.myboard2.mybatis.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.naver.myboard2.domain.Board;

/*
	Mapper �������̽��� ���� ���Ͽ� ����� SQL�� ȣ���ϱ� ���� �������̽��Դϴ�.
	MyBatis-Spring�� Mapper �������̽��� �̿��ؼ� ���� SQL ó���� �Ǵ� Ŭ������ �ڵ����� �����մϴ�.
*/

@Mapper
public interface BoardMapper {

	// ���� ���� ���ϱ�
	public int getListCount();

	// �� ��� ����
	public List<Board> getBoardList(HashMap<String, Integer> map);

	// �� ���� ����
	public Board getDetail(int num);

	// �� �亯
	public int boardReply(Board board);

	// �� ����
	public int boardModify(Board modifyboard);

	// �� ����
	public int boardDelete(Board board);

	// ��ȸ�� ������Ʈ
	public int setReadCountUpdate(int num);

	// �۾��� Ȯ��
	public Board isBoardWriter(HashMap<String, Object> map);

	// �� ����ϱ�
	public void insertBoard(Board board);

	// BOARD_RE_SEQ�� ����
	public int boardReplyUpdate(Board board);
	
	
	public List<String> getDeleteFileList();
	
	public void deleteFileList(String filename);

}
