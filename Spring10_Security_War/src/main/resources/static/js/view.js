$(function(){

	$("#content").keyup(function(){
		let content = $(this).val();
		 const length= content.length;
		 if(length > 50){
		 	length = 50;
		 	content = content.substring(0,length);
		 	$(this).val(content);
		 }
		 
       $("#comment_limit_text").text(length+'/50');
	})
	
	
	$("#message").click(function(){
		getList(++page);
	})// 더 보기 클릭 시 page 내용 추가


	$(document).on('click','.update',function(){
		const tr = $(this).parent().parent();
		$(tr).css('background', 'lightgray');
		
		num = $(this).next().next().val();
		const before= $(this).parent().prev().text();
		
		$("#content").focus().val(before);
		$("#write").text('수정완료');
		$('#comment tr').css('background', '#fff');
		
		
		if(!$('#write').prev().is(".cancel"))
			$(".float-right").before('<button class="btn btn-danger float-right cancel">취소</button>');
		
		$(".remove").prop("disabled",true);
	
	})
	
	$("#comment").on('click','.cancel',function(){
		$("#comment tr").removeAttr('style');
		
		
		$(this).remove();
		$("#write").text("등록");
		$("#content").val('');
		$(".remove").prop("disabled",false);
	
	})
	

	$(document).on('click','.remove',function(){
		const deleteNum = $(this).next().val();
	
		console.log(deleteNum);
		if (confirm("정말 삭제하시겠습니까?")){
			
			$.ajax({
				type : "post",
				url :"../comment/delete",
				data : {
					"num" : deleteNum
				},
				beforeSend: function (jqXHR, settings) {
		           var header = $("meta[name='_csrf_header']").attr("content");
		           var token = $("meta[name='_csrf']").attr("content");
		           jqXHR.setRequestHeader(header, token);
				},
				success : function(result){
					if(result == 1){
						alert("삭제되었습니다");
						getList(page);
					}else{
						alert("삭제실패");
					}
				
				}
			})
			
		}
	
	})


	$("#write").click(function(){
		const content = $("#content").val().trim();		
		if(!content){
			alert('내용을 입력하세요');
			return false;
		}
		const buttonText = $("#write").text(); // 버튼의 라벨로 add할지 update할지 결정
	
		$(".float-left").text('총 50자까지 가능합니다.');
	
		
		if(buttonText == "등록"){
			url ="../comment/add";
			data = {
				"content" : content,
				"id" : $("#loginid").text(),
				"board_num" : $("#board_num").val()
			};
		}else{
			url ="../comment/update";
			data = {
				"num" : num,
				"content" : content
			};
			$("#write").text("등록");
			$("#comment .cancel").remove();
			
		}
		
		
		
		$.ajax({
			type : "post",
			url : url,
			data : data,
			beforeSend: function (jqXHR, settings) {
	           var header = $("meta[name='_csrf_header']").attr("content");
	           var token = $("meta[name='_csrf']").attr("content");
	           jqXHR.setRequestHeader(header, token);
			},
			success : function(result){
				$("#content").val('');
				getList(page);
			}
		})
		
		
	
	
	})//write








	$("#comment table").hide();
	let page =1;
	
	const count = parseInt($("#count").text());
	
	if(count != 0){
		getList(1);
	}else{
		$("#message").text('등록된 댓글이 없습니다.');
	}

	let num=0;
	let url='';
	let data={};
	
	
	function getList(currentPage){
		$.ajax({
			type:"post",
			url : "../comment/list",
			data:{
				"board_num" : $("#board_num").val(),
				"page" : currentPage
			},
			beforeSend: function (jqXHR, settings) {
	           var header = $("meta[name='_csrf_header']").attr("content");
	           var token = $("meta[name='_csrf']").attr("content");
	           jqXHR.setRequestHeader(header, token);
			},
			dataType : "json",
			success : function(rdata){
				$("#count").text(rdata.listcount);
				if(rdata.listcount > 0){
					$("#comment table").show();
					$("#comment tbody").empty();
					
					$(rdata.list).each(function(){
						let output ='';
						let img = '';
						if($("#loginid").text() == this.id){
							img = "<img src='../resources/image/pencil2.png' width='15px' class='update'>"
								+ "<img src='../resources/image/delete.png' width='15px' class='remove'>"
								+ "<input type='hidden' value=' "+this.num + "'>";
						
						}
						output += "<tr><td>" + this.id + "</td>";
						output += "<td></td>"; //2
						//XSS를 방지하기 위해  <td>영역을 만든 뒤 3번에서 text()안에
						//this.content를 넣어 스크립트를 문자열로 만듭니다.
						
						//2번과 3번을 이용하지않고 4번을 이용하면 내용에 스크립트가 있는경우 스크립트 실행
						//output += "<td> + this.content +"</td>"; 4
						output  += "<td>" + this.reg_date+img +"</td></tr>" ;
				
						$("#comment tbody").append(output);
						
						$("#comment tbody tr:last").find("td:nth-child(2)").text(this.content); //3
						
					})// each end
					
					
					if(rdata.listcount > rdata.list.length){ //전체댓글 갯수 > 현재까지 보여준 댓글 갯수
						$("#message").text("더보기");
					}else{
						$("#message").text("");
					}
					
				} else{
					$("#message").text("등록된 댓글이 없습니다.");
					$("#comment table").hide();
				}
			
			}
		})
	}
	
	
})

