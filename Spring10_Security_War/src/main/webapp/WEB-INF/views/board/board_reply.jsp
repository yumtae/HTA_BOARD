<%@ page language="java" contentType="text/html; charset=UTF-8"%>


<html>
<head>
<title>MVC 게시판</title>
<jsp:include page="header.jsp"/>
<script src="${pageContext.request.contextPath}/resources/js/reply.js"></script>

</head>
<body>
   
   
   <div class="container">
   	<form action ="replyAction" method="post" name="replyform">
   		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
   		 <input type="hidden"  value="${boarddata.BOARD_RE_REF}" name="BOARD_RE_REF"> 
   		 <input type="hidden"  value="${boarddata.BOARD_RE_LEV}" name="BOARD_RE_LEV"> 
   		 <input type="hidden"  value="${boarddata.BOARD_RE_SEQ}" name="BOARD_RE_SEQ"> 
   	
   		<h1>MVC 게시판 - Reply</h1>
   	
   		<div class='form-group'>
			<label for="BOARD_NAME">
				글쓴이
			</label>
			<input name="BOARD_NAME" id="BOARD_NAME" value="" 
			readOnly type="text" class="form-control" >
		</div>
		
		<div class='form-group'>
			<label for="BOARD_SUBJECT">
				제목
			</label>BOARD_SUBJECT
			<textarea name="BOARD_SUBJECT" id="BOARD_SUBJECT" rows="1"
			 class="form-control" maxlength="100">${boarddata.BOARD_SUBJECT}</textarea>
		</div>
		
		
		<div class='form-group'>
			<label for="BOARD_CONTENT">
				내용
			</label>
			<textarea name="BOARD_CONTENT" id="BOARD_CONTENT" rows="10"
			 class="form-control" ></textarea>
		</div>
   	
   		<div class='form-group'>
			<label for="BOARD_PASS">
				비밀번호
			</label>
			<input type='password' name="BOARD_PASS" id="BOARD_PASS" class="form-control" >
		</div>
   	
   		<div class='form-group'>
			<button type="submit" class="btn btn-primary">등록</button>
			<button type="button" class="btn btn-danger" onclick="history.go(-1)">취소</button>
		</div>
		
   	
   	</form>
   	
   	
    
     
     
   </div>
</body>
</html>