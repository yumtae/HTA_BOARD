$(function(){

	
	$("#BOARD_NAME").val( $("#loginid").text() );
	
	$("form[name=boardform]").submit(function(){
		
		if($.trim($("#BOARD_PASS").val()) == ""){
			alert("비밀번호를 입력하세요");
			$("#BOARD_PASS").focus();
			return false;
			
		}
		
		if($.trim($("#BOARD_SUBJECT").val()) == ""){
			alert("제목을 입력하세요");
			$("#BOARD_SUBJECT").focus();
			return false;
			
		}
		
		if($.trim($("#BOARD_CONTENT").val()) == ""){
			alert("내용을 입력하세요");
			$("#BOARD_CONTENT").focus();
			return false;
			
		}
		
		
		
	})
	
	
	
})
