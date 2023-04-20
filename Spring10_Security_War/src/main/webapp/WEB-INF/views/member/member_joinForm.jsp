<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <script src="http://code.jquery.com/jquery-latest.min.js"></script>
<style type="text/css">
	body {
	font-family: Arial, Helvetica, sans-serif;
}

* {
	box-sizing: border-box;
}

select, input[type=text], input[type=password], input[type="button"] {
	width: 100%;
	padding: 15px;
	margin: 5px 0 22px 0;
	display: inline-block;
	border: none;
	background: #f1f1f1;
	outline: none;
	height: 40px;
}

select {
	padding: 10px 15px 10px 15px;
	vertical-align: bottom;
}

legend {
	text-align: center;
	font-size: 40px;
	font-weight: bold
}






input[type=text]:focus, input[type=password]:focus {
	background-color: #ddd;
	outline: none;
}

input[type=button]:hover {
	opacity: 0.8;
	cursor: pointer
}

button {
	background-color: #4CAF50;
	color: white;
	padding: 14px 20px;
	margin: 8px 0;
	border: none;
	cursor: pointer;
	width: 100%;
	opacity: 0.9;
}

button:hover {
	opacity: 1;
}

.cancelbtn {
	padding: 14px 20px;
	background-color: #f44336;
}

.cancelbtn, .signupbtn {
	float: left;
	width: 50%;
}


body {
	background-color: #474e5d;
}

#myform {
	background-color: #fefefe;
	margin: 5% auto 15% auto;
	width: 850px;
	padding: 16px;
}

label {
	display: block;
	font-weight: bold;
}

</style>

<script>
   $(function(){
      let checkid=false;
      let checkemail=false;
      
      $('input[name=id]').on('keyup',
            function(){
               $("#message").empty(); //처음에 pattern에 적합하지 않은 경우 메시지 출력후 적합한 데이터를 입력해도
                                 //계속 같은 데이터 출력하므로 이벤트 시작할 때마다 비워둡니다.
               //[A-Za-z0-9_]의 의미가 \w
               const pattern = /^\w{5,12}$/;
               const id = $("input:eq(1)").val();
               if(!pattern.test(id)){
                  $("#message").css('color', 'red')
                            .html("영문자 숫자 _로 5~12자 가능합니다.");
                  checkid=false;
                  return;
               }
               
               $.ajax({
                  url : "idcheck",   // 상대 경로이기 때문에 join 대신 idcheck 들어감 
                  data : {"id" : id},     // 회원가입 폼에서 입력한 데이터 id가지고 idcheck.net으로 
                  success : function(resp){ 
                     if(resp == -1){ // db에 해당하는 id가 없는 경우
                        $("#message").css('color','green').html("사용 가능한 아이디 입니다.");
                        checkid = true;
                     } else { // db에 해당하는 id가 있는 경우(0)
                        $("#message").css('color','bule').html("사용중인 아이디 입니다.");
                         checkid = false;
                     }
                  }
               }); // ajax end 
      }) // id keyup end
      
      $("input[name=email]").on('keyup',
            function(){
               //$("#email_message").empty();
               //[A-Za-z0-9_]와 동일한 것이 \w입니다.
               //+는 1회 이상 반복을 의미하고 {1,}와 동일합니다.
               //\w+는 [A-Za-z0-9_]를 1개이상 사용하라는 의미입니다.
               const pattern = /^\w+@\w+[.]\w{3}$/;
               const email_value = $(this).val();
               console.log(email_value)
               if(!pattern.test(email_value)){
                  $("#email_message").css('color','red')
                                 .html("이메일형식이 맞지 않습니다.");
                  checkemail=false;
               } else {
                  $("#email_message").css('color','green')
                                    .html("이메일형식이 맞습니다.");
                  checkemail=true;
               }
      }); // email keyup 이벤트처리 끝
      
      $('form').submit(function(){
         if(!$.isNumeric($("input[name='age']").val())){
            alert("나이는 숫자를 입력하세요");
            $("input[name='age']").val('').focus();
            return false;
         }
         
         if(!checkid){
            alert("사용가능한 id로 입력하세요.");
            $("input[name=id]").val('').focus();
            return false;
         }
         
         if(!checkemail){
            alert("email 형식을 확인하세요");
            $("input[name=email]").focus();
            return false;
         }
      }); // submit 
   })// ready
</script>

<meta charset="UTF-8">

<body>
    <div class="container">
  <form name=joinform method="POST" action="joinprocess" 
         id="myform" >
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
     <fieldset>
      <legend>회원가입</legend>
       <label for="id">ID</label>
       <div>
        <input type="text" placeholder="Enter id" name="id" id="id" value='test'>   
        <span id="message"></span>
       </div>
       
       <label for="pass">Password</label>
       <input type="password" placeholder="Enter Password" name="password" id="password" value='1234' >

		 <label for="id">이름</label>
       <div>
        <input type="text" placeholder="Enter name" name="name" id="name" value="test">   
       </div>

     
		 <label for="age">나이</label>
       <div>
        <input type="text" placeholder="Enter age" name="age" id="age" value="25">   
       </div>
       
        <label for="gender">성별</label>
       <div style='margin-bottom:20px'>
        	<input type="radio" name="gender" value="남" checked><span>남자</span> 
        	<input type="radio" name="gender" value="여" ><span>여자</span> 
       </div>

       <label for="email">E-Mail</label>      
       <input type="text" name="email" id="email" value='dsa@da.com' required>
       <span id="email_message"></span> 
  
 
        
      <div class="clearfix">
        <button type="submit" class="signupbtn">회원가입</button>
        <button type="reset"  class="cancelbtn">취소</button>
      </div>
      </fieldset>
  </form>
    </div>


</body>
</html>
    
    