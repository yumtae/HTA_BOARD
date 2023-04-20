$(function(){
	
	let check=0;
	

	$("form[name=modifyform]").submit(function(){
		

		
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
		 
		if($.trim($("#BOARD_PASS").val()) == ""){
			alert("비밀번호를 입력하세요");
			$("#BOARD_PASS").focus();
			return false;
			
		}		
		
		if(check== 0){
			const value = $('#filevalue').text();
			const html = "<input type='hidden' value='"+ value+ "' name='check'>  ";
			console.log(html);
			$(this).append(html);
		}
		
		
		
	})
	
	
	function show(){
		// 파일 이름이 있는 경우 remove가 나타나고 없는경우 가림
		
		if($('#filevalue').text() ==''){
			$('.remove').hide();
			
		}else{
			$('.remove').css({'display':'inline-block',
							'position':'relative',
							'top' : '-5px'});
			
		}
		
		
	}
	
	show();	
	
	
	
	
	$("#upfile").change(function(){
		check++;
		
		const inputfile = $(this).val().split('\\');
		$("#filevalue").text(inputfile[inputfile.length -1 ]);
		show();
		console.log(check);
	})
	
	
	$(".remove").click(function(){
		$('#filevalue').text('');
		$(this).css('display','none');
		$('#upfile').val(''); // 만ㅇ냑 파일을 선택하고 remove 이미지를 클릭하면 <input type=file> 의 값도 빈 문자열로만들어요
		
	})
	
	

	
})
