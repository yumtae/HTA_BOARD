<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix ="c"  uri="http://java.sun.com/jsp/jstl/core" %>

<head>
<jsp:include page="header.jsp"/>
<script src='${pageContext.request.contextPath}/resources/js/modifyform.js'></script>
<style>
	h1{font-size:1.5rem;text-laign:center;color:#1a92b9}
	.container{width:60%}
	label{font-weight:bold}
	#upfile{displaY:none}
	img{width:20px;}

</style>
<script>
	if("${result}" == 'passFail'){
		alert("비밀번호가 다릅니다");
	}
</script>

</head>
<body>
	<div class='container'>
		<form action="modifyAction" method="post" enctype="multipart/form-data" name="modifyform">
		
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
			
			<input type="hidden" name="BOARD_NUM" value="${boarddata.BOARD_NUM }">
			<input type="hidden" name="BOARD_FILE" value="${boarddata.BOARD_FILE}">
			<h1>MVC 게시판 - 수정</h1>
		
			<div class='form-group'>
				<label for="BOARD_NAME">
					글쓴이
				</label>
				<input  value="${boarddata.BOARD_NAME }" NAME="BOARD_NAME" readOnly type="text" class="form-control" >
			</div>
		
		
		
			
			<div class='form-group'>
				<label for="BOARD_SUBJECT">
					제목
				</label>
				<textarea name="BOARD_SUBJECT" id="BOARD_SUBJECT" rows='1'
					  class="form-control" maxlength="100">${boarddata.BOARD_SUBJECT }</textarea>
			</div>
			
			<div class='form-group'>
				<label for="BOARD_CONTENT">
					내용
				</label>
				<textarea name="BOARD_CONTENT" id="BOARD_CONTENT" rows="15" class="form-control" >${boarddata.BOARD_CONTENT }</textarea>
			</div>
		
			<%--원문 글일경우만 수정 가능 --%>
			<c:if test="${boarddata.BOARD_RE_LEV == 0 }">
				<div class='form-group'>
					<label for="board_file">파일 첨부</label>
					<label for="upfile">
						<img src="${pageContext.request.contextPath}/resources/image/attach.png" alt="파일첨부">	
					</label>
					<input type="file" id="upfile" name="uploadfile">
					<span id="filevalue">${boarddata.BOARD_ORIGINAL}</span>
					<img src="${pageContext.request.contextPath}/resources/image/remove.png" alt="파일삭제" width='10px' class="remove">
				</div>
			</c:if>
		
			<div class='form-group'>
				<label for="BOARD_PASS">
					비밀번호
				</label>
				<input name="BOARD_PASS" id="BOARD_PASS" size="10" maxlength="30"
					 type="password" class="form-control" placeholder="Enter board_pass">
			</div>
		
			<div class='form-group'>
				<button type="submit" class="btn btn-primary">수정</button>
				<button type="reset" class="btn btn-danger" onclick="history.go(-1)">취소</button>
			</div>
		</form>
	
	</div>
</body>
