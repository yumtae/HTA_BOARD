function go(page){
	const limit = $("#viewcount").val();
	const data = `limit=${limit}&state=ajax&page=${page}`;
	//const data = {limit:limit,state:"ajax",page="page"}
	ajax(data);
}

//총 2페이지 페이징 처리된 경우
//이전 1 2 다음
//현재 페이지가 1페이지인 경우 아래와 같은 페이징 코드가 필요
//<li class="page-item"><a class="page-link gray">이전&nbsp;</a></li>
//<li class="page-item active"><a class="page-link">1</a></li>
//<li class="page-item"><a class="page-link" href="javascript:go(2)">2</a></li>
//<li class="page-item"><a class="page-link" href="javascript:go(2)">다음&nbsp;</a></li>

//현재 페이지가 2페이지인 경우 아래와 같은 페이징 코드가 필요
//<li class="page-item"><a class="page-link" href="javascript:go(1)">이전&nbsp;</a></li>
//<li class="page-item"><a class="page-link" href="javascript:go(1)">1</a></li>
//<li class="page-item active"><a class="page-link">2</a></li>
//<li class="page-item"><a class="page-link gray">다음&nbsp;</a></li>

function setPaging(href, digit){
	let active = "";
	let gray="";
	if(href == ""){
		if(isNaN(digit)){
			gray = "gray";
		}else{
			active="active";
		}
	}
	
	let output = `<li class="page-item ${active}">`;
	let anchor = `<a class='page-link ${gray}' ${href}>${digit}</a></li>`;
	output += anchor;
	return output;
	
}


function ajax(sdata){
	console.log(sdata);
	
	$.ajax({
		type: "POST",
		url: "BoardList.bo",
		data: sdata,
		dataType: "json",
		cache : false,
		success:function(data){
			$("#viewcount").val(data.limit);
		
			$("thead").find("span").text("글 개수 : " + data.listcount)
			
			if(data.listcount > 0){
				$("tbody").remove();
				let num = data.listcount - (data.page-1) * data.limit;
				console.log(num);
				let output = "<tbody>";
				$(data.boardlist).each(
					function(index,item){
						output += '<tr><td>' + (num--) + '</td>'
						
						const blank_count = item.board_re_lev * 2 + 1;
						let blank ="&nbsp;";
						for (let i = 0; i <blank_count;i++){
							blank += '&nbsp;&nbsp;'
						}
						
						let img="";
						if(item.board_re_lev>0){
							img ="<img src='image/line.gif'>";
						}
					
						let subject = item.board_subject;
						if(subject.length >=20){
							subject = subject.substr(0,20) + "...";
						}
							
						output += "<td><div>" + blank + img
						output += ' <a href="BoardDetailAction.bo?num='+item.board_num + '">'
						output += subject.replace(/</g,'&lt;').replace(/>/g,'&gt;')
									+ '</a>[' +item.cnt + ']</div></td>'
						output += '<td><div>' + item.board_name +'</td></div>'
						output += '<td><div>' + item.board_date +'</td></div>'
						output += '<td><div>' + item.board_readcount +'</td></div></td>'
						
					}//function(index,item)
				); //each
				
				output += "</tbody>"
				$('table').append(output); //테이블 완성
				
				$(".pagination").empty(); 
				output ="";
				
				let digit = '이전&nbsp;'
				let href="";
				if(data.page > 1){
					href = 'href=javascript:go(' + (data.page-1) + ')';
				}
				output += setPaging(href,digit);
				
				for(let i = data.startpage; i <= data.endpage ; i++){
					digit = i;
					href="";
					if( i != data.page){
						href = 'href=javascript:go(' + i + ')';
					}
					output += setPaging(href,digit);
				} 
				
				digit = '&nbsp;다음&nbsp;';
				href="";
				if(data.page < data.maxpage){
					href = 'href=javascript:go(' + (data.page +1) + ')';
				}
				output +=setPaging(href,digit);
				$('.pagination').append(output);
				
				
			} //if(data.listcount > 0)
			
		} ,//success:function 
		error : function(){
			console.log("에러");
		}
		
	}) //ajax end
		
	
}//function end


$(function(){
	
	$("button").click(function(){
		location.href="BoardWrite.bo";
		
	})
	
	$("#viewcount").change(function(){
		go(1); //보여줄 페이지를 1페이지로 설정
		
	})
	
})