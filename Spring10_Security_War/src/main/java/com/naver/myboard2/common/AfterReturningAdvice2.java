package com.naver.myboard2.common;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/*
 * Advice : 횡단 관심에 해당하는 공통 기능을 의미하며 독립된 클래스의 메서드로 작성
 * AfterReturningAdvice : 아켓 메소드가 성공적으로 결과값을 반환 후에 어드바이스 기능을 수행합니다
 * */

//@Service
//@Aspect
public class AfterReturningAdvice2 {

	private static final Logger logger = LoggerFactory.getLogger(AfterReturningAdvice2.class);
			
	@AfterReturning(pointcut = "execution(* com.naver.myboard2..*Impl.get*(..))",
					returning="obj")
	public void afterReturningLog(Object obj) {

		
		logger.info("=============================================================");
		logger.info( "[AfterReturningAdvice] obj :" + obj.toString());
		logger.info("=============================================================");
	}
	
	
}
