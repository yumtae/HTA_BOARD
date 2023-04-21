<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>가입 페이지</title>
 <script src="http://code.jquery.com/jquery-latest.min.js"></script>
 <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
 <link rel='stylesheet' href="${pageContext.request.contextPath}/css/join.css">

<script>

	$(function(){
		let checkid=false;
		let checkemail=false;
		
		$('input[name=id]').on('keyup',function(){
			
			$('#message').empty(); // 처음 pattern에 적합하지 않은 경우 메시지 출력
			
			//[A-Za-zz0-9_]의 의미가 \w
			
			const pattern = /^\w{5,12}$/;
			const id = $("input:eq(0)").val();
			
			if(!pattern.test(id))	{
				$('#message').css('color','red').html("영문자 숫자 _ 로 5~12자 가능합니다.");
				checkid=false;
				return;
				
			}
			
			
			$.ajax({
				url: "idcheck.net",
				data : {"id" : id},
				success : function(resp){
					
					if(resp == -1){ //db에 id가 없을경우
						$("#message").css('color','green').html("사용 가능한 아이디입니다");
						checkid = true;
						
					}else{
						$("#message").css('color','red').html("사용중인 아이디입니다");
						checkid = false;
					}
					
				}
			
			})//ajax
			
			
			
			
			
		})//keyup
		
		
		$('input[name=email]').on('keyup',function(){
			
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
		
		
		})//email 키업
		
		
		$('form').submit(function(){
				if(!$.isNumeric($("input[name='age']").val())){
					alert("나이는 숫자를 입력하세요");
					$("input[name='age']").val('').focus();
					return false;
				
				}
			
				if(!checkid){
					alert("사용가능한 id로 입력하세요");
					$("input[name='id']").val('').focus();
					return false;
				
				}
				
				
				if(!checkemail){
					alert("email 형식을 확인하세요");
					$("input[name='email']").val('').focus();
					return false;
				
				}
	
		
		})
		
		
		
		
		
	})


</script>

</head>
<body>

  <form name="joinform" method="post" action="joinProcess.net" id="joinform" >
     <h1>회원가입</h1>
      <hr/>
      
      <b>아이디</b>
      <input type="text" name="id" placeholder ="enter id" maxlength="12" required>
      <span id="message"></span>
      
      <b>비밀번호</b>
      <input type="password" name="pass" placeholder ="enter pass"  required>
      <b>이름</b>
      <input type="text" name="name" placeholder ="enter name" required>
      <b>나이</b>
      <input type="text" name="age" placeholder ="enter age" maxlength="2" required>
      
       <b>성별</b>
       <div>
       	<input type='radio' name='gender' value='남' checked ><span>남자</span>
       	<input type='radio' name='gender' value='여'  ><span>여자</span>
       </div>
       
        <b>이메일주소</b>
      	<input type='text' name='email'  placeholder ="enter email"  maxlength="30" required>
     	<span id ="email_message"></span>
     	<div class='clearfix'>
     		<button type="submit" class="submitbtn">회원가입</button>
     		<button type="reset" class="cancelbtn">다시작성</button>
     	</div>
     
     
  </form>
  

</body>
</html>