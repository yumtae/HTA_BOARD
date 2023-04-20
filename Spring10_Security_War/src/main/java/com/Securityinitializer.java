package com;


import javax.servlet.ServletContext;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.support.MultipartFilter;


//������ ���� ���� Ŭ������ �߰��ϴ� ������ ���������� DelegatingFilterProxy�� �������� ����մϴ�
public class Securityinitializer  extends AbstractSecurityWebApplicationInitializer{

	//��ť��Ƽ ���� �տ� ���� ���͵��� ���ʴ�� �߰��մϴ�
	@Override
	protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
		
		CharacterEncodingFilter characterEncodingfilter = new CharacterEncodingFilter();
		characterEncodingfilter.setEncoding("UTF-8");
		characterEncodingfilter.setForceEncoding(true);
		insertFilters(servletContext, characterEncodingfilter);
		
		MultipartFilter multipartFilter = new MultipartFilter();
		multipartFilter.setMultipartResolverBeanName("multipartResolver");
		insertFilters(servletContext, multipartFilter);
		
	}

		
	
}
