package com.naver.myboard2.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AfterThrowingAdvice {

	private static final Logger logger = LoggerFactory.getLogger(AfterThrowingAdvice.class);
			
	
	public void afterThrowingLog(Throwable exp) {

		
		logger.info("=============================================================");
		logger.info("[AfterThrowing] : 비즈니스 로직 수행 중 오류가 발생하면 동작합니다." );
		logger.info("ex: " + exp.toString() );
		logger.info("=============================================================");
	}
	
	
}
