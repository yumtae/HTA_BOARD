package com.naver.myboard2.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AfterThrowingAdvice {

	private static final Logger logger = LoggerFactory.getLogger(AfterThrowingAdvice.class);
			
	
	public void afterThrowingLog(Throwable exp) {

		
		logger.info("=============================================================");
		logger.info("[AfterThrowing] : ����Ͻ� ���� ���� �� ������ �߻��ϸ� �����մϴ�." );
		logger.info("ex: " + exp.toString() );
		logger.info("=============================================================");
	}
	
	
}
