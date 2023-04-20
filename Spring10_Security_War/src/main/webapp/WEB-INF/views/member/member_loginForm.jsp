<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix ="c"  uri="http://java.sun.com/jsp/jstl/core" %>   

<!DOCTYPE html>
<html>
<head>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>
	
	$(function(){
		$('.join').click(function(){
			location.href="${pageContext.request.contextPath}/member/join";
			
		})
	})
		
		
		if("${result}" == 'joinSuccess'){
			alert("회원가입을 축하합니다");
		}else if("${loginfail}" == 'loginFailMsg'){
			alert("아이디나 비밀번호가 오류입니다");
		}
	
	
</script>



<title>회원관리 시스템 로그인 페이지</title>
<link href='${pageContext.request.contextPath}/resources/css/login.css' type='text/css' rel="stylesheet">
</head>
<body>
	
  <div class="container">

	<form name='loginform' action="${pageContext.request.contextPath}/member/loginProcess" 
	method="post"  style="border:1px solid #ccc;padding:30px">
	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
	    <label for="id"><b>아이디</b></label>
	    <input type="text" placeholder="Enter id" id='id' name="id" required 
	    	<c:if test="${!empty saveid }">
	    		value="${saveid }
	    	</c:if>
	    >
		
	    <label for="passwd"><b>비밀번호</b></label>
	    <input type="password" placeholder="Enter password" name="password" required>
	
		<div class='form-group custom-control custom-checkbox'>
			<input type='checkbox' class='custom-control-input' id='remember-me' name='remember-me' 
				<c:if test="${!empty saveid }">
		    		checked
		    	</c:if>
			>
			<label class='custom-control-label' for="remember-me" >로그인 유지하기</label>
		</div>
	
	    <div class="clearfix">
	     <button type="submit" class="signupbtn" >로그인</button>
	     <button type="button" class="join" style='width:100%'>회원가입</button>
	     
	    </div>

	</form>
  </div>
</body>
</html>
    
    
    