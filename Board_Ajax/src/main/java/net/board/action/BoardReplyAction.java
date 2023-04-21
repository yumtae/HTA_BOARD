package net.board.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.board.db.BoardBean;

public class BoardReplyAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request , HttpServletResponse response)
		throws ServletException, IOException{
			ActionForward forward = new ActionForward();
			BoardDAO boarddao= new BoardDAO();
			BoardBean boarddata= new BoardBean();
			int result =0;
			
			boarddata.setBoard_name(request.getParameter("board_name"));
			boarddata.setBoard_pass(request.getParameter("board_pass"));
			boarddata.setBoard_subject(request.getParameter("board_subject"));
			boarddata.setBoard_content(request.getParameter("board_content"));
			
			boarddata.setBoard_re_ref(Integer.parseInt(request.getParameter("board_re_ref")));
			boarddata.setBoard_re_lev(Integer.parseInt(request.getParameter("board_re_lev")));
			boarddata.setBoard_re_seq(Integer.parseInt(request.getParameter("board_re_seq")));
		
			
			//답변을 DB에 담기위해 boarddata 객체를 파라미터로 전달하고
			//DAO의 메서드를 호출
			result = boarddao.boardReply(boarddata);
		
			//답변에 실패한 경우
			if(result == 0) {
				System.out.println("답장 저장 실패");
				//forward = new ActionForward();
				forward.setRedirect(false);
				request.setAttribute("message", "답장 저장 실패입니다");
				forward.setPath("error/error.jsp");
				return forward;

			}
			
			System.out.println("답장 완료");
			forward.setRedirect(true);
			forward.setPath("BoardDetailAction.bo?num="+result);
			return forward;

	
		}
	
}
