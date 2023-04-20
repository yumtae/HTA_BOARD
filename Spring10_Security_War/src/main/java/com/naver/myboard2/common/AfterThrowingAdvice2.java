package com.naver.myboard2.common;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Aspect

//getBoardList�ȿ� double i = 1/0 �� �־� �׽�Ʈ�մϴ�
public class AfterThrowingAdvice2 {

	
	private static final Logger logger = LoggerFactory.getLogger(AfterThrowingAdvice2.class);
			
	@AfterThrowing(pointcut = "execution(* com.naver.myboard2..*Impl.get*(..))" , throwing = "exp")
	public void afterThrowingLog(Throwable exp) {

		
		logger.info("=============================================================");
		logger.info("[AfterThrowing] : ����Ͻ� ���� ���� �� ������ �߻��ϸ� �����մϴ�." );
		logger.info("ex: " + exp.toString() );
		logger.info("=============================================================");
	}
	
	
}
