<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix ="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원관리 시스템 회원수정 페이지</title>
 <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
 <link rel='stylesheet' href="${pageContext.request.contextPath}/resources/css/join.css">
	<style>
	
		h3{text-align:center;color:#1a92b9}
		input[type=file]{
			display:none
		}
	</style>

</head>
<body>

<jsp:include page="../board/header.jsp"/>
  <form name="joinform" method="post" action="updateProcess" id="joinform"  method="post" >
  	<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
     <h3>회원 정보 수정</h3>
      <hr/>
      
      <b>아이디</b>
      <input type="text" name="id" readonly value="${memberinfo.id }">

      
      <b>비밀번호</b>
      <input type="password" name="password" value="${memberinfo.password }"  readonly>
      <b>이름</b>
      <input type="text" name="name" value="${memberinfo.name }" placeholder ="enter name"  required>
      <b>나이</b>
      <input type="text" name="age" placeholder ="enter age" value="${memberinfo.age }" maxlength="2" required>
      
       <b>성별</b>
       <div>
       	<input type='radio' name='gender' value='남'  ><span>남자</span>
       	<input type='radio' name='gender' value='여'  ><span>여자</span>
       </div>
       
        <b>이메일주소</b>
      	<input type='text' name='email'  value="${memberinfo.email }" placeholder ="enter email"  maxlength="30" required>
     	<span id="email_message"></span>
     	
     	
     	
     	
     	<div class='clearfix'>
     		<button type="submit" class="submitbtn">수정</button>
     		<button type="button" class="cancelbtn">취소</button>
     	</div>
     
     
  </form>
  
  
  <script>
  
  
  	$("input[value='${memberinfo.gender}']").prop('checked',true);
  	
  	$(".cancelbtn").click(function(){
  			history.back();
  		
  	})
  	
  	let checkemail  = true;
  	$('input:eq(6)').on('keyup',function(){
			
			// +는 1회 이상반복을 의미하고 {1,}와 동일합니다
			//\w+ 는 [A-Za-zz0-9_]를 1개이상 사용하라는 의미
			const pattern = /^\w+@\w+[.]\w{3}$/;
			const email_value = $(this).val();
			if(!pattern.test(email_value)){
				
				$("#email_message").css('color','red').html("이메일 형식이 맞지 않습니다");
				checkemail = false;
				
			}else{
				$("#email_message").css('color','green').html("이메일 형식에 맞습니다");
				checkemail = true;
			}
  	})
  	
  	
  	
  	$('form[name=joinform]').submit(function(){
			if(!$.isNumeric($("input[name='age']").val())){
				alert("나이는 숫자를 입력하세요");
				$("input[name='age']").val('').focus();
				return false;
			
			}
						
			if(!checkemail){
				alert("email 형식을 확인하세요");
				$("input[name='email']").val('').focus();
				return false;
			
			}
	
	})
  	
	
	
  	
  </script>
  

</body>
</html>