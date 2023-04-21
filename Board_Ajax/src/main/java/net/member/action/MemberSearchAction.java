package net.member.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.member.db.Member;
import net.member.db.MemberDAO;

public class MemberSearchAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			
			ActionForward forward = new ActionForward();
			MemberDAO mdao = new MemberDAO();
		
			int page = 1;	
			int limit = 3;
			
			if(request.getParameter("page") != null) {
				page = Integer.parseInt(request.getParameter("page"));
			}
			System.out.println("넘어온 페이지=" + page);
			
			List<Member> list = null;
			int listcount  = 0;
			int index= -1; //search_field에 존재하지 않을 값으로 초기화
			
			String search_word="";
			
			
			//메뉴-관리자-회원정보를 클릭한 경우 (memberList.net)
			//또는 메뉴-관리자-회원정보 클릭 후 페이지를 클릭한 경우
			//(memberList.net?page=2&search_field=-1&search_word=)
			
			if(request.getParameter("search_word") ==null 
					|| request.getParameter("search_word").equals("")) {
				//총 리스트수를 받아옵니다
				listcount = mdao.getListCount();
				list = mdao.getList(page,limit);
			}else {
				//System.out.println("search_field = " + request.getParameter("search_field"));
				//System.out.println("search_word = " + request.getParameter("search_word"));
				
				index = Integer.parseInt(request.getParameter("search_field"));
				String[] search_field = new String[] {"id","name","age","gender"};
				search_word = request.getParameter("search_word");
				listcount = mdao.getListCount(search_field[index],search_word);
				list = mdao.getList(search_field[index],search_word,page,limit);
			}
			
			int maxpage = (listcount+ limit -1) /limit;
			System.out.println("총 페이지 수 = " + maxpage);
			
			int startpage = ((page-1) / 10)* 10 + 1;
			System.out.println("현재 페이지에 보여줄 시작 페이지 = " + startpage);
			int endpage = startpage+10 -1;
			
			System.out.println("현재 페이지에 보여줄 마지막 페이지 수 = " + endpage);
			if(endpage > maxpage) {
				endpage =maxpage;
			}
			
			request.setAttribute("page", page);
			request.setAttribute("maxpage", maxpage);
			request.setAttribute("startpage", startpage);
			request.setAttribute("endpage", endpage);
			request.setAttribute("listcount", listcount);
			request.setAttribute("totallist", list);
			request.setAttribute("search_field", index);
			request.setAttribute("search_word", search_word);
			forward.setPath("member/memberList.jsp");
			forward.setRedirect(false);
			return forward;
	
	}

}
