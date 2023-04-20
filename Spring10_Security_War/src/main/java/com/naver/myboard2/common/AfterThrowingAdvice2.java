package com.naver.myboard2.common;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Aspect

//getBoardList안에 double i = 1/0 를 넣어 테스트합니다
public class AfterThrowingAdvice2 {

	
	private static final Logger logger = LoggerFactory.getLogger(AfterThrowingAdvice2.class);
			
	@AfterThrowing(pointcut = "execution(* com.naver.myboard2..*Impl.get*(..))" , throwing = "exp")
	public void afterThrowingLog(Throwable exp) {

		
		logger.info("=============================================================");
		logger.info("[AfterThrowing] : 비즈니스 로직 수행 중 오류가 발생하면 동작합니다." );
		logger.info("ex: " + exp.toString() );
		logger.info("=============================================================");
	}
	
	
}
