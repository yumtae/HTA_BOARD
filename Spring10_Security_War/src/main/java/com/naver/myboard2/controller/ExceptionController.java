package com.naver.myboard2.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;


/*
 	@ControllerAdvice �ֳ����̼��� ���ؼ� �� Ŭ������ ��ü�� ��Ʈ�ѷ����� �߻��ϴ�	
 		Exception�� ���������� ó���ϴ� Ŭ������� ���� ���
 	
 		����� ����� ������ �����ϴ�.
 		
 	1. Ŭ������  @ControllerAdvice ��� �ֳ����̼� ó��
 	2. �� �޼ҵ忡 @ExceptionHandler�� �̿��ؼ� ������ Ÿ���� Exception�� ó��
 	
 * */

@ControllerAdvice
public class ExceptionController {

	
	private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);
	
	/* common �޼ҵ�� Exception Ÿ������ ó���ϴ� ��� ���ܸ� ó���ϵ��� ���� */
	@ExceptionHandler(Exception.class)
	public ModelAndView common(Exception e, HttpServletRequest request) {
		logger.info(e.toString());
		ModelAndView mav = new ModelAndView();
		mav.setViewName("error/common");
		mav.addObject("exception",e);
		mav.addObject("url" ,request.getRequestURL());
		return mav;
	}
	
	
	@ExceptionHandler(NoHandlerFoundException.class)
	public ModelAndView handelError404(Exception e, HttpServletRequest request) {
		logger.info(e.toString());
		ModelAndView mav = new ModelAndView();
		mav.setViewName("error/404");
		mav.addObject("exception","404 ���� �߻��߽��ϴ�.");
		mav.addObject("url" ,request.getRequestURL());
		return mav;
	}
	
	
}
