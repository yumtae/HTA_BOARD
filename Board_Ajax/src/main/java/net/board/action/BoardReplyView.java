package net.board.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.board.db.BoardBean;

public class BoardReplyView implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request , HttpServletResponse response)
		throws ServletException, IOException{
			
			ActionForward forward = new ActionForward();
			BoardDAO boarddao= new BoardDAO();
			BoardBean boarddata= new BoardBean();
			forward = new ActionForward();
			
			int num = Integer.parseInt(request.getParameter("num"));
			
			boarddata = boarddao.getDetail(num);
			if(boarddata == null) {
				System.out.println("글이 존재하지 않습니다");
				
				forward.setRedirect(false);
				request.setAttribute("message", "글이 존재하지 않습니다");
				forward.setPath("error/error.jsp");
				return forward;

			}
		

			System.out.println("답변 페이지 이동 완료");
			request.setAttribute("boarddata", boarddata);
			forward.setRedirect(false);
			forward.setPath("board/boardReply.jsp");
			return forward;
			

	
		}
	
}
