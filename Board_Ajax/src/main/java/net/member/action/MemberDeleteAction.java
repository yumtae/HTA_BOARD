package net.member.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.member.db.MemberDAO;

public class MemberDeleteAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		MemberDAO mdao = new MemberDAO();
		String id = request.getParameter("id") ;
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		int result = mdao.delete(id); 
		
		out.print("<script>"); 
		if(result==1) {
			out.print("alert('삭제 성공');"); 
			out.print("location.href='memberList.net'"); 
		}else {
			out.print("alert('삭제 실패');"); 
			out.print("history.back()"); 
		}
		out.print("</script>"); 
		
		out.close();
		return null;
	}

}
