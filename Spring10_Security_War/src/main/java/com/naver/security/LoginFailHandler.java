package com.naver.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

//AuthenticationFailureHandler : �α��� ���� �� ó���� �۾��� �ؾ��� �� ����ϴ� �������̽�

//@Service
public class LoginFailHandler implements AuthenticationFailureHandler {
	private static final Logger logger  = LoggerFactory.getLogger(LoginFailHandler.class);

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		HttpSession session = request.getSession();
		logger.info(exception.getMessage());
		logger.info("�α��� ����");
		session.setAttribute("loginfail", "loginFailMsg");
		String url =  request.getContextPath()+ "/member/login";
		response.sendRedirect(url);
	}
	
	
}
