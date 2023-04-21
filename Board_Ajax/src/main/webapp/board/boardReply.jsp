<%@ page language="java" contentType="text/html; charset=UTF-8"%>


<html>
<head>
<title>MVC 게시판</title>
<jsp:include page="header.jsp"/>
<script src="js/reply.js"></script>

</head>
<body>
   
   
   <div class="container">
   	<form action ="BoardReplyAction.bo" method="post" name="replyform">
   		 <input type="hidden"  value="${boarddata.board_re_ref}" name="board_re_ref"> 
   		 <input type="hidden"  value="${boarddata.board_re_lev}" name="board_re_lev"> 
   		 <input type="hidden"  value="${boarddata.board_re_seq}" name="board_re_seq"> 
   	
   		<h1>MVC 게시판 - Reply</h1>
   	
   		<div class='form-group'>
			<label for="board_name">
				글쓴이
			</label>
			<input name="board_name" id="board_name" value="${id}" 
			readOnly type="text" class="form-control" >
		</div>
		
		<div class='form-group'>
			<label for="board_subject">
				제목
			</label>
			<textarea name="board_subject" id="board_subject" rows="1"
			 class="form-control" maxlength="100">${boarddata.board_subject}</textarea>
		</div>
		
		
		<div class='form-group'>
			<label for="board_content">
				내용
			</label>
			<textarea name="board_content" id="board_content" rows="10"
			 class="form-control" ></textarea>
		</div>
   	
   		<div class='form-group'>
			<label for="board_pass">
				비밀번호
			</label>
			<input type='password' name="board_pass" id="board_pass" class="form-control" >
		</div>
   	
   		<div class='form-group'>
			<button type="submit" class="btn btn-primary">등록</button>
			<button type="button" class="btn btn-danger" onclick="history.go(-1)">취소</button>
		</div>
		
   	
   	</form>
   	
   	
    
     
     
   </div>
</body>
</html>