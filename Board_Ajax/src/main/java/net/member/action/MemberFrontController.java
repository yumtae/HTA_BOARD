package net.member.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("*.net")
public class MemberFrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
    public MemberFrontController() {
        super();
    }

    private void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	/*
    	 요청된 전체 URL 중에서 포트번호 다음 부터 마지막 문자열까지 반환합니다
    	 예) contextPath 가 "/JspProject" 인 경우
    	 	http://localhost:8080/JSPProject/login.nee 으로 요청하면 getRequestURI()는
    	 	"JSPProject/login.net"을 반홥합니다
    	  
    	 * */
    	
    	String RequestURI = request.getRequestURI();
    	System.out.println("RequestURI = " + RequestURI);
    	
		//getContextPath() : 컨텍스트 경로가 반환
    	// ContextPath는 "/JspProject"가 반환
    	String contextPath = request.getContextPath();
    	System.out.println("contextPath = " + contextPath);
    	
    	//RequestURI에서 컨특스트 경로 길이 값의 인덱스 위치의 문자부터 마지막 위치 문자까지 추출합니다
    	// command는 "/login.net"이 반환됩니다
    	String command = RequestURI.substring(contextPath.length());
    	System.out.println("command = " + command);
    	
    	
    	
    	//초기화
    	ActionForward forward = null;
    	Action action = null;
    	
    	
    	switch(command) {
	    	case "/login.net":
	    		action = new MemberLoginAction();
	    		break;
	    		
	    	case "/join.net":
	    		action = new MemberJoinAction();
	    		break;	
	    		
	    	case "/idcheck.net":
	    		action = new MemberIdCheckAction();
	    		break;	
	    		
	    	case "/joinProcess.net":
	    		action = new MemberJoinProcessAction();
	    		break;	
	    		
	    	case "/loginProcess.net":
	    		action = new MemberLoginProcessAction();
	    		break;
	    	case "/logout.net":
	    		action = new MemberLogOutAction();
	    		break;
	    		
	    	case "/memberUpdate.net":
	    		action = new MemberUpdateAction();
	    		break;
	    		
	    	case "/updateProcess.net":
	    		action = new MemberUpdateProcessAction();
	    		break;
	    	case "/memberList.net":
	    		action = new MemberSearchAction();
	    		break;
	    		
	    	case "/memberInfo.net":
	    		action = new MemberInfoAction();
	    		break;
	    		
	    	case "/memberDelete.net":
	    		action = new MemberDeleteAction();
	    		break;
	    		
	    		
	    		
    	}
    	
    	forward = action.execute(request,response);
    	 
    	if(forward !=null) {
    		if(forward.isRedirect()) {
    			response.sendRedirect(forward.getPath());
    		
    		
    	    }else {
    		   RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
    		    dispatcher.forward(request, response);
    	    }
    	}
    	
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		doProcess(request, response);
	}

}
