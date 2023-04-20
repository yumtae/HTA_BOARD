package com;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.naver.security.CustomAccessDeniedHandler;
import com.naver.security.CustomUserDetailsService;
import com.naver.security.LoginFailHandler;
import com.naver.security.LoginSuccessHandler;

@EnableWebSecurity // �������� ��ť��Ƽ ����
@Configuration
public class SecurityConfig  extends WebSecurityConfigurerAdapter{

	//<security:http > �����κ�
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		 http.authorizeRequests()
         .antMatchers("/resources/**").permitAll()
         .antMatchers("/member/login").permitAll()
         .antMatchers("/member/join").permitAll()
         .antMatchers("/member/idcheck").permitAll()
         .antMatchers("/member/joinprocess").permitAll()
         .antMatchers("/member/list").access("hasRole('ROLE_ADMIN')")
         .antMatchers("/member/info").access("hasRole('ROLE_ADMIN')")
         .antMatchers("/**").access("hasAnyRole('ROLE_MEMBER', 'ROLE_ADMIN')");
		
		   http.formLogin().loginPage("/member/login")
				           .loginProcessingUrl("/member/loginProcess")
				           .usernameParameter("id")
				           .passwordParameter("password")
				           .successHandler(loginSuccessHandler())
				           .failureHandler(loginFailHandler());

		

						
			/* 1)logoutSuccessUrl : �α׾ƿ� �� �̵� �ּ�
			 * 2)logoutUrl :  ���⼭ ó���ϱ� ������ ��Ʈ�ѷ� logout ���� . post��� �䱸
			 * 3)invalidateHttpSession : �α׾ƿ��� ���� �Ӽ��� ����
			 * 4) deleteCookies : ��Ű����
			 * 
			 * */

			http.logout().logoutSuccessUrl("/member/login")
			.logoutUrl("/member/logout")
			.invalidateHttpSession(true)
			.deleteCookies("remember-me","JESSION_ID");
		
			http.rememberMe()
			.rememberMeParameter("remember-me")
			.rememberMeCookieName("remember-me")
			.tokenValiditySeconds(2419200);
	
			http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
	}
	
	


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(customUserService()).passwordEncoder(passwordEncoder());
	}


	//�α��� ������ ������ ��ü ����
	 @Bean
	   public AuthenticationSuccessHandler loginSuccessHandler() {
	      return new LoginSuccessHandler(); // ��Ű�� com.naver.security�� �����ϴ� ��� Ŭ���� ���Ͽ� @Service ����
	   }

	
	/*
 	1. UserDetailsService �������̽��� DB���� ���� ������ �ҷ����� loadUserByUsername()�� �����մϴ�
  		�̸� �����ϴ� Ŭ������ DB���� ������ ������ �����ͼ� UserDetails Ÿ������ ������ �ִ� �۾��� �մϴ�.
  		
  	2. UserDetails�� �������̽��� Security���� ������� ������ ��� �������̽��Դϴ�.
  	
  	3. UserDeails �������̽��� ������ Ŭ������ ������ ������� ������ ����ڰ� ���� ������ ������ ó���ؼ� ��ȯ�ϰ� �˴ϴ�.
  			��) UserDetails user = new User(username, user.getPassword(),roles);
 * */
	
	@Bean
	public UserDetailsService customUserService() {
		return new CustomUserDetailsService(); //  // ��Ű�� com.naver.security�� �����ϴ� ��� Ŭ���� ���Ͽ� @Service ����
	}


	//ServletConfig.java ���ִ� passwordEncoder�� �����ɴϴ�
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}
	
	@Bean
	public AuthenticationFailureHandler loginFailHandler() {
		return new LoginFailHandler();
	}

	
	
	
}
