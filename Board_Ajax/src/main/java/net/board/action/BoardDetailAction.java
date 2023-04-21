package net.board.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.board.db.BoardBean;

public class BoardDetailAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request , HttpServletResponse response)
		throws ServletException, IOException{
			
			BoardDAO boarddao = new BoardDAO();
			BoardBean boarddata = new BoardBean();
		
		
			int num =Integer.parseInt(request.getParameter("num"));
			
			
			//내용을 확인할 글의 조회수 증가
			boarddao.setReadCountUpdate(num);
			
			//글의 내용을 DAO에서 읽은 후 얻은 결과를 boarddata 객체에 저장
			boarddata=boarddao.getDetail(num);
		
			
			//error테스트를 위한 값 설정
			// DAO 에서 글의 내용을 읽지 못했을 경우 null을 반환
			if(boarddata == null) {
				
				System.out.println("상세보기 실패");
				ActionForward forward = new ActionForward();
				forward.setRedirect(false);
				request.setAttribute("message", "데이터를 읽지 못했습니다");
				forward.setPath("error/error.jsp");
				return forward;
				
			}
			System.out.println("상세보기 성공");
			
			//boarddata 객체를 request객체에 저장
			request.setAttribute("boarddata", boarddata);
			ActionForward forward = new ActionForward();
			forward.setRedirect(false);
			forward.setPath("board/boardView.jsp"); // 글 내용 보기 페이지로 이동
			return forward;
			
					
	
		}
	
}
