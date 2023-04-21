<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix ="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원관리 시스템 회원수정 페이지</title>
 <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
 <link rel='stylesheet' href="${pageContext.request.contextPath}/css/join.css">
	<style>
	
		h3{text-align:center;color:#1a92b9}
		input[type=file]{
			display:none
		}
	</style>

</head>
<body>

<jsp:include page="../board/header.jsp"/>
  <form name="joinform" method="post" action="updateProcess.net" id="joinform"  enctype="multipart/form-data" >
     <h3>회원 정보 수정</h3>
      <hr/>
      
      <b>아이디</b>
      <input type="text" name="id" readonly value="${memberinfo.id }">

      
      <b>비밀번호</b>
      <input type="password" name="pass" value="${memberinfo.password }"  readonly>
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
     	
     	
     	<b>프로필 사진</b>
     	<label for='memberfile'>
     		<img src="image/attach.png" width="10px">
     		<span id="filename">${memberinfo.memberfile }</span>
     		
     		<span id="showImage">
     		
     			<c:if test="${empty memberinfo.memberfile }">
     				<c:set var ='src' value='image/profile.png'/>
     			</c:if>
     			
     			<c:if test="${!empty memberinfo.memberfile }">
     				<c:set var ='src' value='${"memberupload/"}${memberinfo.memberfile}'/>
     				<input type="hidden" name="check" value="${memberinfo.memberfile}"><%-- 파일이 있는데 변경하지 않는 경우 --%>
     			</c:if>
     			<img src="${src}" width="20px" alt="profile">
     			
     		</span>
     		
     		<%--
     			accept:  업로드할 파일 타입 설정
     			accept="파일 확장자 | audio/* | video/* | image/*"
     			
     		 --%>
     		
     		<input type="file" id="memberfile" name="memberfile" accept="image/*">
     		
     	</label>
     	
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
  	
	
	
	
	
		$('#memberfile').change(function(e){

			const inputfile  = $(this).val().split('\\');
			const filename = inputfile[inputfile.length - 1];
			
			const pattern = /(gif|jpg|jpeg|png)$/i ;   // i(ignore case)는 대소문자를 무시
			
			if(pattern.test(filename)){
				$("#filename").text(filename);
				const reader = new FileReader(); // 파일을 읽기 위한 객체 생성
				
				//DataURL 형식 ( 접두사 data: 가 붙은 URl이며 바이너리 ㅍ ㅏ일을 Base64로 인코딩하여 ASCII 문자열 형식으로 변환한 것)
				//파일을 읽어옵니다. (참고 - Base64 인코딩은 바이너리 데이터를 Text로 변경하는 Encoding 입니다)
				//읽어온 결과는 reader객체의 result 속성에 저장
				//event.target.files[0] - 파일리스트에 첫번째 겍츠를 가져옵니다

				reader.readAsDataURL(e.target.files[0]);
				reader.onload=function(){ //읽기 성공 시 실행되는 이벤트 핸들러
					$("#showImage > img ").attr('src',this.result);
				}
			
			}else{
				alert('이미지 파일이 아닌 경우 무시됩니다.')
				$("#filename").text("");
				$("#showImage > img ").attr('src',"image/profile.png");
				$(this).val('');
				$('input[name=check]').val("");
				
			}
	
	})
	
  	
  </script>
  

</body>
</html>