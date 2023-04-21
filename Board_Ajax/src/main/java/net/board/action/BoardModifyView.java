package net.board.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.board.db.BoardBean;

public class BoardModifyView implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request , HttpServletResponse response)
		throws ServletException, IOException{
			
			ActionForward forward = new ActionForward();
			BoardDAO boarddao= new BoardDAO();
			BoardBean boarddata= new BoardBean();
			
			int num = Integer.parseInt(request.getParameter("num"));
			
			boarddata = boarddao.getDetail(num);
			
			if(boarddata == null) {
				System.out.println("(수정) 상세보기 실패");
				forward = new ActionForward();
				forward.setRedirect(false);
				request.setAttribute("message", "게시판 수정 상세 보기 실패입니다");
				forward.setPath("error/error.jsp");
				return forward;

			}
			System.out.println("(수정)  상세보기 성공");
			request.setAttribute("boarddata", boarddata);
			forward.setRedirect(false);
			forward.setPath("board/boardModify.jsp");
			return forward;
			
			
	
		}
	
}
