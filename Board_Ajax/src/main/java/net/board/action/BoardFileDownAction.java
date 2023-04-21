package net.board.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BoardFileDownAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request , HttpServletResponse response)
		throws ServletException, IOException{
	
			String fileName = request.getParameter("filename");
			System.out.println("파일이름 = " + fileName);
		
			String savePath = "boardupload";
			
			ServletContext context = request.getServletContext();
			String sDownloadPath = context.getRealPath(savePath);
			
			// \ 를 추가하기위해 \\ 를 사용
			String sFilePath = sDownloadPath + "\\" + fileName;
			System.out.println(sFilePath);
			
			byte b[] = new byte[4096];
			
			String sMimeType = context.getMimeType(sFilePath);
			System.out.println("sMimeType >>>" + sMimeType);
			
		
			if(sMimeType == null)
				sMimeType = "application/octet-stream";
			
			response.setContentType(sMimeType);
			
			//한글 깨짐 방지
			String sEncoding = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
			System.out.println(sEncoding);
			
			
			// Content-Disposition :  attachment  : 브라우저는 content를 처리하지않고 다운
			response.setHeader("Content-Disposition", "attachment;filename=" + sEncoding);
			
			
			try(
					//웹브라우저 출력 스트림 생성
					
					BufferedOutputStream out2 = new BufferedOutputStream(response.getOutputStream());
					
					BufferedInputStream in = new BufferedInputStream(new FileInputStream(sFilePath));
					
			){
				int numRead;
				//read(b,0,b.length) : 바이트 b의 0번부터  읽어옴
				while((numRead = in.read(b,0,b.length)) != -1 ){ // 데이터 존재시
					//바이트 배열 b의 0부터  numread크기만큼 브라우저 출력
					out2.write(b,0,numRead);
				}
				
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			
			return null;

	
		}
	
}
