package com.naver.myboard2.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.naver.myboard2.domain.Comment;
@Mapper
public interface CommentMapper {

	public int getListCount(int board_num);

	public List<Comment> getCommentList(Map<String, Integer> map);

	public int commentsInsert(Comment c);

	public int commentsDelete(int num);

	public int commentsUpdate(Comment c);

}
