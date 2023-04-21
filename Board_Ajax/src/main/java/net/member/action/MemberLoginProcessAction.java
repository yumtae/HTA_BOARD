package net.member.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.member.db.MemberDAO;

public class MemberLoginProcessAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		ActionForward forward =  new ActionForward();
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");
		

		MemberDAO dao = new MemberDAO();
		
		int result = dao.isId(id,pass);
		System.out.println("결과는 " + result);
		
		//로그인 성공
		if(result ==1) {
			HttpSession session = request.getSession();
			session.setAttribute("id", id);
			
			String IDstore = request.getParameter("remember");
			Cookie cookie = new Cookie("id",id);
		
			if(IDstore != null && IDstore.equals("store")) {
				cookie.setMaxAge(2*60);
				
				response.addCookie(cookie);
				System.out.println("쿠키확인");
			}else {
				cookie.setMaxAge(0);
				response.addCookie(cookie);
			}
			forward.setRedirect(true);
			forward.setPath("BoardList.bo");
			return forward;
			
		}else {
			String message = "비밀번호가 일치하지 않습니다";
			if(result == -1)
				message = "아이디가 존재하지 않습니다	";
				
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print("<script>");
			out.print("alert('"+message+"');");
			out.print("location.href='login.net';");
			out.print("</script>");
			out.close();
			return null;
		}

	}

}
