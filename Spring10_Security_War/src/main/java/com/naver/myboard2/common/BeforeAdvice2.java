package com.naver.myboard2.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/* ȣ��Ǵ� ����Ͻ� �޼����� ������ JoinPoint �������̽��� �� �� �ֽ��ϴ�.*/

//�������� ó���� ������ BeforeAdvice Ŭ������ beforeLog()�޼���� �����մϴ�.
//Advice : Ⱦ�� ���ɿ� �ش��ϴ� ���� ����� �ǹ��ϸ� ������ Ŭ������ �޼���� �ۼ��˴ϴ�.
//Advice Ŭ������ ������ ���� ���Ͽ��� <bean>���� ����ϰų� @Service annotation�� ����մϴ�

//@Service
//@Aspect
public class BeforeAdvice2 {

	private static final Logger logger = LoggerFactory.getLogger(BeforeAdvice2.class);
			
	@Before("execution(* com.naver.myboard2..*Impl.get*(..))")
	public void beforeLog(JoinPoint proceeding) {

		
		logger.info("=============================================================");
		logger.info("[BeforeAdvice2] : ����Ͻ� ���� ���� �� �����Դϴ�.");
		logger.info("[BeforeAdvice2] " + proceeding.getTarget().getClass().getName() + "��"
					+ proceeding.getSignature().getName() + "() ȣ���մϴ�.");
		logger.info("=============================================================");
	}
	
	
}
