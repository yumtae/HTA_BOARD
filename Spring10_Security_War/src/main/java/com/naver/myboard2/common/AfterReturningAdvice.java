package com.naver.myboard2.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Advice : Ⱦ�� ���ɿ� �ش��ϴ� ���� ����� �ǹ��ϸ� ������ Ŭ������ �޼���� �ۼ�
 * AfterReturningAdvice : ���� �޼ҵ尡 ���������� ������� ��ȯ �Ŀ� �����̽� ����� �����մϴ�
 * */

public class AfterReturningAdvice {

	private static final Logger logger = LoggerFactory.getLogger(AfterReturningAdvice.class);
			
	
	public void afterReturningLog(Object obj) {

		
		logger.info("=============================================================");
		logger.info( "[AfterReturningAdvice] obj :" + obj.toString());
		logger.info("=============================================================");
	}
	
	
}
