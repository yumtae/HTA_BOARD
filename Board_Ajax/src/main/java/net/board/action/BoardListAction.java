package net.board.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.board.db.BoardBean;

public class BoardListAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request , HttpServletResponse response)
		throws ServletException, IOException{
		
		
		BoardDAO boarddao = new BoardDAO();
		List<BoardBean> boardlist = new ArrayList<BoardBean>();
		
		
		//로그인 성공시 파라미터 page가 없어서 초기값이 필요
		int page = 1;	//보여줄 페이지
		int limit = 10; //한 페이지에 보여줄 게시판 목록의 수
		
		if(request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		}
		
		
		System.out.println("넘어온 페이지 = " + page);
		
		
		//추가
		if(request.getParameter("limit") != null) {
			limit = Integer.parseInt(request.getParameter("limit"));
		}

		
		
		//총 리스트 수
		int listcount = boarddao.getListCount();
		
		//리스트를 받아옵니다.
		boardlist = boarddao.getBoardList(page,limit);
		
		/*
		총 페이지 수
				= (DB에 저장된 총 리스트 수 + 한 페이지에서 보여주는 리스트의 수 -1) / 한 페이지에서 보여주는 리스트 수
				
			예를 들어 한 페이지에서 보여주는 리스트의 수가 10개인 경우
			예1) DB에 저장된 총 리스트의 수가 0이면 총 페이지수는 0페이지
			예2) DB에 저장된 총 리스트의 수가 (1~10)이면 총 페이지수는 1페이지
			예2) DB에 저장된 총 리스트의 수가 (11~20)이면 총 페이지수는 2페이지
			예2) DB에 저장된 총 리스트의 수가 (21~30)이면 총 페이지수는 3페이지
		
		 * */
		int maxpage = (listcount + limit - 1) / limit;
		System.out.println("총 페이지수 = " + maxpage);
			
		/*
		 	startpage : 현제 페이지 그룹에서 맨 처음에 표시될 페이지 수
		 	([1], [11], [21] 등..) 보여줄 페이지가 30개일 경우
		 	[1][2][3]...[30]까지 다 표시하기엔 너무 만히깨둔메 보통 10개
		 	예) 페이지 그룹이 아래와 같은 경우
		 		[1][2][3][4][5][6][7][8][9][10]
		 	페이지그룹의 시작 페이지는 startpage에 마지막 페이지는 endpage에 구합니다
		  
		 */
		
		int startpage = ((page-1) / 10)* 10 + 1;
		System.out.println("현재 페이지에 보여줄 시작 페이지 = " + startpage);
		
		//endpage : 현제 페이지 그룹에서 보여줄 마지막 페이지수 [10][20][30] 등
		int endpage = startpage+10 -1;
		System.out.println("현재 페이지에 보여줄 마지막 페이지 수 = " + endpage);
		
		
		/*
		 	마지막 그룹의 마지막 페이지 값은 최대 페이지값
		 	예로 페이지 그룹이 [21]~[30]일 경우
		  	시작 페이지는 21(startpage=21) 와 마지막 페이지는 30(endpage=30) 이지만
		  	최대 페이지(maxpage)가 25라면 [21]~[25]까지만 표시되도록 합니다
		 * */
		
		if(endpage > maxpage) {
			endpage =maxpage;
		}
		
		String state = request.getParameter("state");
		
		if(state ==null) {
			System.out.println("state == null");
			request.setAttribute("page",page ); //현제 페이지
			request.setAttribute("maxpage",maxpage ); //최대 페이지
			
			//현재 페이지에 표시할 첫 페이지 수
			request.setAttribute("startpage",startpage );
			
			//현재 페이지에 표시할 마지막 페이지 수
			request.setAttribute("endpage", endpage);
			
			request.setAttribute("listcount",listcount ); //총 글수
			
			// 해당 페이지의 글 목록을 갖고 있는 리스트
			request.setAttribute("boardlist", boardlist);
			
			request.setAttribute("limit", limit);
			ActionForward forward = new ActionForward();
			forward.setRedirect(false);
			
			//글 목록 페이지로 이동하기 위해 경로를 설정
			forward.setPath("board/boardList.jsp");
			return forward;
			
		}else {
			
			System.out.println("state=ajax");
			
			JsonObject object = new JsonObject();
			object.addProperty("page", page);
			object.addProperty("maxpage", maxpage);
			object.addProperty("startpage", startpage);
			object.addProperty("endpage", endpage);
			object.addProperty("listcount", listcount);
			object.addProperty("limit",limit );
			
			
			//JsonObject에 list형식을 담을 수 있는 addProperty() 존재하지 않음
			//void com.google.gson.JsonObject.add(String property, JsonElement value) 메서드를 통해 저장
			//List 형식을 JsonElement로 바꾸어야 object에 저장 가능
			
			
			//List => JsonElement
			JsonElement je = new Gson().toJsonTree(boardlist);
			System.out.println("boardlist = " + je.toString());
			object.add("boardlist", je);
			
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().print(object);
			System.out.println(object.toString());
			return null;
			
		}
		
		
		
		
		
	}
	
	
}
