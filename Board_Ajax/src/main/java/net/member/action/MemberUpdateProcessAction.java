package net.member.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import net.member.db.Member;
import net.member.db.MemberDAO;

public class MemberUpdateProcessAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
			String realFolder="";
			String saveFolder="memberupload";
			
			int fileSize=5*1024*1024;
			ServletContext sc = request.getServletContext();
			realFolder = sc.getRealPath(saveFolder);
			System.out.println("realFolder = " + realFolder);
			
			try {
				MultipartRequest multi = new MultipartRequest(
						request, 
						realFolder,
						fileSize,
						"utf-8",
						new DefaultFileRenamePolicy());
				
		
				String id = multi.getParameter("id");
				String name = multi.getParameter("name");
				int age = Integer.parseInt(multi.getParameter("age")) ;
				String gender = multi.getParameter("gender");
				String email = multi.getParameter("email");
				String memberfile = multi.getFilesystemName("memberfile");
				
				Member m = new Member();
				
				m.setId(id);m.setName(name);
				m.setAge(age);m.setGender(gender);m.setEmail(email);				
				m.setMemberfile(memberfile);
				
				if(memberfile != null) {  //파일을 선택한 경우
					m.setMemberfile(memberfile);
				}else if(multi.getParameter("check") != ""){ //기존파일을 그대로 사용할 경우
					m.setMemberfile(multi.getParameter("check"));
				}
				
				MemberDAO mdao = new MemberDAO();
				int result = mdao.update(m);
				
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out = response.getWriter();
				out.print("<script>");
				//삽입된 경우
				if(result ==1) {
					out.print("alert('수정되었습니다');");
					out.print("location.href='BoardList.bo';");
				}else {
					out.print("alert('수정에 실패했습니다');");
					out.print("history.back()");
				}
				
				out.print("</script>");
				out.close();
				return null;
				
			}catch (IOException e) {
				ActionForward forward = new ActionForward();
				forward.setPath("error/error.jsp");
				request.setAttribute("message", "프로필 사진 업로드 실패입니다.");
				forward.setRedirect(false);
				return forward;
			}
		
	}

}
