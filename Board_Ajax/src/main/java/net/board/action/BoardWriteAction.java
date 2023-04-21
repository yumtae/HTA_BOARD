package net.board.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class BoardWriteAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request , HttpServletResponse response)
		throws ServletException, IOException{
			
			ActionForward forward = new ActionForward();
			forward.setRedirect(false);// 주수 변경없이 jsp 페이지 내용 보여줍니다
			forward.setPath("board/boardWrite.jsp");
					return forward;

	
		}
	
	
}
