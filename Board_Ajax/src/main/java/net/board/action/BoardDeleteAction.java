package net.board.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BoardDeleteAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request , HttpServletResponse response)
		throws ServletException, IOException{
			
			boolean result= false;
			boolean userchkeck= false;
	
			int num = Integer.parseInt(request.getParameter("num"));
		
			BoardDAO boarddao =  new BoardDAO();
			//글 삭제 명령을 요청한 사용자가 글을 작성한 사용자인지 판단하기위해
			//입력한 비밀번호와 저장된 비밀번호를 비교하여 일치하면 삭제
			userchkeck = boarddao.isBoardWriter(num, request.getParameter("board_pass"));
	
			//일치하지 않을 경우
			if(userchkeck==false) {
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('비밀번호가 다릅니다');");
				out.println("history.back();");
				out.println("</script>");
				out.close();
				return null;
			}
			
			result = boarddao.boardDelete(num);
			
			//삭제 실패한 경우
			if(result==false) {
				System.out.println("게시판 삭제 실패");
				ActionForward forward = new ActionForward();
				forward.setRedirect(false);
				request.setAttribute("message", "데이터를 삭제하지 못했습니다");
				forward.setPath("error/error.jsp");
				return forward;
			}
			
			System.out.println("게시판 삭제 성공");
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('삭제 되었습니다.');");
			out.println("location.href='BoardList.bo';");
			out.println("</script>");
			out.close();
			return null;
		}
	
}
