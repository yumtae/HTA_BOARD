package com.naver.myboard2.controller;

import java.io.PrintWriter;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.naver.myboard2.domain.MailVO;
import com.naver.myboard2.domain.Member;
import com.naver.myboard2.service.MemberService;
import com.naver.myboard2.task.SendMail;

/*
  @Component�� �̿��ؼ� ������ �����̳ʰ� �ش� Ŭ���� ��ü�� �����ϵ��� ������ �� ������
  ��� Ŭ������ @Component�� �Ҵ��ϸ� � Ŭ������ � ������ �����ϴ��� �ľ��ϱ�
  ��ƽ��ϴ�. ������ �����ӿ�ũ������ �̷� Ŭ�������� �з��ϱ� ���ؼ�
  @Component�� ����Ͽ� ������ ���� �� ���� �ֳ����̼��� �����մϴ�.
  
  
  1. @Controller - ������� ��û�� �����ϴ� Controller Ŭ����
  2. @Repository - ������ ���̽� ������ ó���ϴ� DAOŬ����
  3. @Service - ����Ͻ� ������ ó���ϴ� Service Ŭ����
  
 * */



@Controller
@RequestMapping(value="/member")
public class MemberController2 {
	//import org.slf4j.Logger;
	//import org.slf4j.LoggerFactory;
	private static final Logger logger = LoggerFactory.getLogger(MemberController2.class);
	
	private MemberService memberservice;
	private PasswordEncoder passwordEncoder;
	private SendMail sendMail;
	
	@Autowired
	public MemberController2(MemberService memberservice, PasswordEncoder passwordEncoder, SendMail sendMail) {
			this.memberservice = memberservice;
			this.passwordEncoder=passwordEncoder;
			this.sendMail = sendMail;
			
	}

	/*	CookieValue(value="saveid", required=false) Cookie readCookie
	 * 	�̸��� saveid�� ��Ű�� Cookie Ÿ������ ���޹޽��ϴ�
	 * ������ �̸��� ��Ⱑ �������� ���� �� �ֱ⋚����  required=false �����ؾ��մϴ�
	 * ��, id ������⸦ �������� ���� ���� �ֱ� ������ required=false �����ؾ� �մϴ�
	   required=true ���¿��� ������ �̸��� ���� ��Ⱑ �������� ������ ������ MVC�� �ͼ����� �߻���ŵ�ϴ�
	 * */
	
	//http://localhost:8088/myboard/member/login
	//�α��� ���̵�
	@RequestMapping(value="/login" , method= RequestMethod.GET)
	public ModelAndView login(ModelAndView mv,
								@CookieValue(value="remember-me",required=false) Cookie readCookie,
								HttpSession session,
								Principal userPrincipal
								) {
		if(readCookie != null) {
				logger.info("����� ���̵�: " + userPrincipal.getName());
			mv.setViewName("redirect:/board/list");
		}else {
			
		
			mv.setViewName("member/member_loginForm");
			mv.addObject("loginfail", session.getAttribute("loginfail"));
			session.removeAttribute("loginfail");
		}
		return mv;
	}
	
	
	
	
	//http://localhost:8088/myboard/member/join
	//ȸ������ �� �̵�
	@RequestMapping(value="/join", method=RequestMethod.GET)
	public String join() {
		return "member/member_joinForm"; //WEB-INF/views/member/member_joinForm.jsp
		
	}
	
	
	//ȸ������������ ���̵� �˻�
	@RequestMapping(value="/idcheck", method=RequestMethod.GET)
	public void idcheck(@RequestParam("id") String id ,
							HttpServletResponse response) throws Exception { 
			
			System.out.println(id);
			int result = memberservice.isId(id);
			
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(result);
		
	}
	
	//ȸ������
	@RequestMapping(value="/joinprocess", method=RequestMethod.POST)
	public String joinprocess(Member m,
							RedirectAttributes rattr,
							Model model,
							HttpServletRequest request)  { 
		System.out.println("���Խ���");
		//��й�ȣ ��ȣȭ �߰�
		String encPassword = passwordEncoder.encode(m.getPassword());
		logger.info(encPassword);
		m.setPassword(encPassword);
		
		int result = memberservice.insert(m);
		System.out.println(result);
		//�����׽�Ʈ result=0;
		
		/*
		 * ���������� �����ϴ� RedirectAttributes�� ������ Servlet���� ���Ǵ�
		 	response.sendRedirect()�� ����� ���� ������ �뵵�� ����մϴ�.
		 	�����̷�Ʈ�� �����ϸ� �Ķ���͸� �����ϰ��� �� �� addAttribute()�� addFlashAttribute()�� ����մϴ�
		 	��)response.sendRedirect("/test?result=1);
		 		=>rattr.addAttribute("result" , 1);
		 
		 * */
		
		if(result ==1 ) {
			
			MailVO vo = new MailVO();
			vo.setTo(m.getEmail());
			vo.setContent(m.getId() + "�� ȸ�� ������ ���ϵ帳�ϴ�.");
			sendMail.sendMail(vo);
			
			rattr.addFlashAttribute("result" , "joinSuccess");
			return "redirect:login"; 
		}else {
			model.addAttribute("url",request.getRequestURL());
			model.addAttribute("message","ȸ�� ���� ����");
			return "error/error"; 
		}
		
	
		
	}

	
	//��ť��Ƽ������ ���������� 
	//@RequestMapping(value="/loginProcess" , method= RequestMethod.POST)
	public String loginProcess(@RequestParam("id") String id,@RequestParam("password") String password,
									@RequestParam(value="remember", defaultValue="", required=false) 
												String remember,
									HttpServletResponse response,
									HttpSession session,
									RedirectAttributes rattr
								) {
		int result = memberservice.isId(id, password);
		logger.info("��� : " + result);
		
		if(result == 1) {
			//�α��� ����
			session.setAttribute("id", id);
			Cookie saveCookie = new Cookie("saveid",id);
					
				if(!remember.equals("")) {
					saveCookie.setMaxAge(60*60);
					logger.info("�������: 60*60");
				}else {
					logger.info("��Ű ���� : 0");
					saveCookie.setMaxAge(0);	
				}
				//��Ű�� ������ �������� ��������Ѵ�
				response.addCookie(saveCookie);
				return "redirect:/board/list";
		}else {
			rattr.addFlashAttribute("result",result);
			return "redirect:login";
		}
		
		
	}
	
	//��ť��Ƽ���� �����ϱ⿡ ����
	//@RequestMapping(value="/logout" , method= RequestMethod.GET)
	public String loginout(HttpSession session) {
		session.invalidate();
		return "redirect:login";
		
	}
	
	
	
	@RequestMapping(value="/update" , method= RequestMethod.GET)
	public ModelAndView member_update(Principal principal,
										ModelAndView mv) {
		String id = principal.getName();
		
		if(id ==null) {
			mv.setViewName("redirect:login");
			logger.info("id is null");
		}else {
				Member m = memberservice.member_info(id);
				mv.addObject("memberinfo",m);
				mv.setViewName("member/member_updateForm");
		}
		
		return mv;
		
	}
	
	
	@RequestMapping(value="/updateProcess" , method= RequestMethod.POST)
	public String member_updateProcess(Member member, Model model,
										HttpServletRequest request,
										RedirectAttributes rattr) {
		System.out.println(member.getAge());
		int result = memberservice.update(member);
		
		if(result ==1) {
			rattr.addFlashAttribute("result", "updateSuccess");
			return "redirect:/board/list";
		}else {
			model.addAttribute("url", request.getRequestURL());
			model.addAttribute("message", "���� ���� ����");
			return "error/error";
		}
		
	}
	
	
	@RequestMapping(value="/list")
	public ModelAndView memberList(
					@RequestParam(value="page", defaultValue =  "1", required=false) int page,
					@RequestParam(value="limit", defaultValue = "3", required=false) int limit,
					ModelAndView mv,
					@RequestParam(value="search_field", defaultValue = "-1", required=false) int index,
					@RequestParam(value="search_word", defaultValue="", required=false) String search_word
			) 
			{
		
			int listcount = memberservice.getSearchListCount(index, search_word);
			List<Member> list = memberservice.getSearchList(index, search_word, page, limit);
		
			//�� ������ ��
			int maxpage = (listcount + limit -1) /limit ;
			
			// ���� ������ ������ ���� ������ (1,11,21...)
			int startpage = ((page - 1) / 10 ) * 10 +1;
			
			//���� �������� ������ ������ ������ �� (10,20,30...)
			int endpage = startpage +10 -1;
			
			if(endpage > maxpage)
					endpage = maxpage;
			
			
			
			mv.setViewName("member/member_list");
			mv.addObject("page",page);
			mv.addObject("maxpage",maxpage);
			mv.addObject("startpage",startpage);
			mv.addObject("endpage",endpage);
			mv.addObject("listcount",listcount);
			mv.addObject("memberlist",list);
			mv.addObject("search_field",index);
			mv.addObject("search_word",search_word);
			return mv;
		
	}
	
	
	@RequestMapping(value="/info" , method= RequestMethod.GET)
	public ModelAndView member_info(@RequestParam("id") String id,
									ModelAndView mv,
									HttpServletRequest request){
		
		Member m = memberservice.member_info(id);
		if(m !=null) {
			System.out.println("info�������̵�");
			mv.setViewName("member/member_info2");
			mv.addObject("memberinfo",m);
		}else {
				mv.addObject("url",request.getRequestURL());
				mv.addObject("message" , "�ش� ������ �����ϴ�.");
				mv.setViewName("error/error");
		}
		
		return mv;
	}
	
	
	@RequestMapping(value="/delete" , method= RequestMethod.GET)
	public String member_delete(String id) {
		memberservice.delete(id);
		return "redirect:list";
		
	}
			

	
	
}
