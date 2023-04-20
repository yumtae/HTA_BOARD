package com.naver.myboard2.common;


import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

//Advice : Ⱦ�� ���ɿ� �ش��ϴ� ���� ����� �ǹ��ϸ� ������ Ŭ������ �޼���� �ۼ��˴ϴ�.
//Around Advice : ����Ͻ� �޼��� ���� ���̳� �Ŀ� ����Ǵ� Advice

@Service
@Aspect
@EnableAspectJAutoProxy
public class AroundAdvice2 {

	private static final Logger logger = LoggerFactory.getLogger(AroundAdvice2.class);
	/*
	  1. ProceedingJoinPoint �������̽���  JoinPoint�� ����߱� ������ JoinPoint�� ���� ��� �޼��带 �����մϴ�.
	  2. Around Advice ������ ProceedingJoinPoint �� �Ű������� ����ϴµ� �� ������ procceed()�޼��尡 �ʿ��ϱ� �����Դϴ�
	  3. Around Advice �� ��� ����Ͻ� �޼��� ���� ���� �Ŀ� ���� �Ǵµ� ����Ͻ� �޼��带 ȣ���ϱ� ���ؼ���
	  		ProceedingJoinPoint �� proceed()�޼��尡 �ʿ��մϴ�
	  		��, Ŭ���̾�Ʈ�� ��û�� ����æ �����̽��� Ŭ���̾�Ʈ�� ȣ���� ����Ͻ� �޼���(ServiceImpl�� get���� �����ϴ� �޼���)��
	  			ȣ���ϱ� ���� ProceedingJoinPoint ��ü�� �Ű� ������ �޾� proceed()�޼��带 ���ؼ�
	  			����Ͻ� �޼��带 ȣ���� �� �ֽ��ϴ�
	  			
	  * proceed()�޼��� ���� �� �޼ҵ��� ��ȯ���� �����ؾ� �մϴ�
	 * */
	
	@Around("execution(* com.naver.myboard2..*Impl.update*(..))")
	public Object aroundLog(ProceedingJoinPoint proceeding) throws Throwable {
	
		
		logger.info("=============================================================");
		logger.info("[AroundAdvice] : ����Ͻ� �޼��� ���� ���Դϴ�.");
		logger.info("=============================================================");
		StopWatch sw = new StopWatch();
		sw.start();
		
		// �� �ڵ带 �������� ���� �ķ� �����ϴ�
		Object result = proceeding.proceed();
		
		sw.stop();
		
		logger.info("=============================================================");
		logger.info("[AroundAdvice] : ����Ͻ� �޼��� ���� ���Դϴ�.");
		
		
		//ȣ��Ǵ� �޼��忡 ���� ������ ���մϴ�.
		Signature sig = proceeding.getSignature();
		
		
		/*
		 * import org.aspectj.lang.Signature �������̽��� ȣ��Ǵ� �޼���� ���õ� ������ �����մϴ�.
		 * String getName() : �޼����� �̸��� ���Ѵ�.
		 * */
		
		
		/*
		 * 1.  proceeding.getTarget().getClass().getSimpleName() : TargetŬ������ �̸��� �����ɴϴ�.
		 * 2. Object[] getArgs() : Ŭ���̾�Ʈ�� �޼��带 ȣ���� �� ������ ���� ����� Object �迭�� �����մϴ�
		 * */
		
		//Object[] getArgs() :  Ŭ���̾�Ʈ�� �޼��带 ȣ���� �� �Ѱ��� ���� ����� Object �迭�� �����մϴ�.
		
		
		// BoardServiceImpl.getBoardList([1,10])
		logger.info("[Around Advice�� after] : " +  proceeding.getTarget().getClass().getSimpleName() + "." + 
							sig.getName() + "(" + Arrays.toString( proceeding.getArgs()) + ")"   );
		
		//getBoardList() �޼ҵ� ���� �ð�  3��
		logger.info("[Around Advice�� after] : " + proceeding.getSignature().getName() + "() �޼ҵ� ���� �ð� : "
					+sw.getTotalTimeMillis() + "(ms)��");
		
		// com.naver.myboard2.BoardServiceImpl
		logger.info("[Around Advice�� after] : " + proceeding.getTarget().getClass().getName());
		
		logger.info("proceeding.proceed() ���� �� ���Ѱ�" + result);
		
		
		logger.info("=============================================================");
		
	
		return result;
		
	}
	
	
}
