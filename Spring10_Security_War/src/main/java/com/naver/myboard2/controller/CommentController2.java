package com.naver.myboard2.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naver.myboard2.domain.Comment;
import com.naver.myboard2.service.CommentService;

@RestController
@RequestMapping(value="/comment")
public class CommentController2 {

	
	private CommentService commentService;
	
	@Autowired
	public CommentController2( CommentService commentservice) {
			this.commentService = commentservice;
		
	}
	
	private static final Logger logger = LoggerFactory.getLogger(CommentController2.class);
	
	
	//@ResponseBody
	@PostMapping(value="/list")
	public Map<String,Object> CommentList(int board_num , int page) {
		List<Comment> list = commentService.getCommentList(board_num, page);

		int listcount = commentService.getListCount(board_num);
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("listcount", listcount);
		return map;
		
	}
	
	
	//@ResponseBody
	@PostMapping(value="/add")
	public int CommentAdd(Comment c) {
		return commentService.commentInsert(c);		
		
		
	}
	
	//@ResponseBody
	@PostMapping(value="/update")
	public int CommentUpdate(Comment c) {
		return commentService.commentUpdate(c);		
		
	}
	
	//@ResponseBody
	@PostMapping(value="/delete")
	public int CommentDelete(int num) {
		return commentService.commentDelete(num);		
		
	}
	
	
}
