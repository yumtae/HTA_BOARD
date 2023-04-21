package net.member.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.member.db.Member;
import net.member.db.MemberDAO;

public class MemberInfoAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		ActionForward forward = new ActionForward();
		String id = request.getParameter("id") ;
		MemberDAO mdao = new MemberDAO(); 
		Member m = mdao.member_info(id);
		
		if(m == null) {
			forward.setPath("error/error.jsp");
			forward.setRedirect(false);
			request.setAttribute("message", "아이디에 해당하는 정보 없음");
			return forward;
		}
		
		forward.setRedirect(false);// 주수 변경없이 jsp 페이지 내용 보여줍니다
		forward.setPath("member/memberInfo.jsp");
		request.setAttribute("memberinfo", m);
				return forward;
	}

}
