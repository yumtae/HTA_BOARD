package net.board.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import net.board.db.BoardBean;

public class BoardModifyAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request , HttpServletResponse response)
		throws ServletException, IOException{
			
		
		BoardDAO boarddao =  new BoardDAO();
		BoardBean boarddata = new BoardBean();
		ActionForward forward = new ActionForward();
		
		String realFolder="";
		String saveFolder="boardupload";
		
		int fileSize=5*1024*1024;
		
		
		//실제 저장 경로 지정
		ServletContext sc = request.getServletContext();
		realFolder = sc.getRealPath(saveFolder);
		System.out.println("realFolder = " + realFolder);
		boolean result=false;
		
		try {
			MultipartRequest multi = new MultipartRequest(
					request, 
					realFolder,
					fileSize,
					"utf-8",
					new DefaultFileRenamePolicy());
			
			int num = Integer.parseInt(multi.getParameter("board_num"));
			String pass = multi.getParameter("board_pass");
			
			boolean usercheck = boarddao.isBoardWriter(num,pass);
			
			if(usercheck == false) {
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('비밀번호가 다릅니다11');");
				out.println("history.back();");
				out.println("</script>");
				out.close();
				return null;
				
			}
			
			
			//BoardBean 객체에 글 등록 폼에서 입력 받은 정보 저장
			boarddata.setBoard_num(num);
			boarddata.setBoard_subject(multi.getParameter("board_subject"));
			boarddata.setBoard_content(multi.getParameter("board_content"));
			
			String check = multi.getParameter("check");
			System.out.println("check = " + check);
			if(check != null) { //파일 첨부를 ㅂ련경하지 않으면
				boarddata.setBoard_file(check);
			}else {
				String filename = multi.getFilesystemName("board_file");
				boarddata.setBoard_file(filename);
				
			}
			

			result = boarddao.boardModify(boarddata);
			
			
			if(result==false) {
				System.out.println("게시판 수정 실패");
				forward.setPath("error/error.jsp");
				request.setAttribute("message", "게시판 수정이 되지 않았습니다");
				forward.setRedirect(false);
				return forward;
			}
			
			System.out.println("게시판 수정 완료");
			
			forward.setRedirect(true);
			
			forward.setPath("BoardDetailAction.bo?num=" + boarddata.getBoard_num());
			System.out.println(forward.toString());
			return forward;
			
			
			}catch (IOException e) {
				e.printStackTrace();
				forward.setPath("error/error.jsp");
				request.setAttribute("message", "게시판 업로드 중 실패입니다.");
				forward.setRedirect(false);
				return forward;
			}
	
		
		
		
		
		
		
	
		}
	
}
