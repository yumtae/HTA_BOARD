<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ taglib prefix ="c"  uri="http://java.sun.com/jsp/jstl/core" %>

<head>

<title>회원 관리 시스템 관리자 모드(회원 목록 보기)</title>
<jsp:include page="../board/header.jsp"/>

<%--
	1. 검색어를 입력한 후 다시 memberList.net으로 온 경우 검색 필드와 검색어가 나타나도록한다
	
	
	2. 검색 필드를 변경하면 검색어 입력창에 placeholder 나타나도록 합니다
		ex) 아이디선택시 placeholder에 "아이디를 입력하세요"
		ex) 이름선택시 placeholder에 "이름을 입력하세요"
		ex) 나이선택시 placeholder에 "나이를 입력하세요"
		ex) 성별선택시 placeholder에 "남 또는 여 입력하세요"


	3. 검색 버튼 클릭시 다음을 체크
		1) 검색어를 입력하지 않은 경우 "검색어를 입력하세요" alert
		2) 나이는 두 자리 숫자가 아닌 경우 "나이는 형식에 맞게 입력하세요(두자리 숫자)" 
		3) 성별은 남 , 여가 아닌경우  "남 또는 여를 입력하세요"
		
	4. 회원 목록의 삭제를 클릭한 경우
		confirm('정말 삭제하시겠습니까?')를 실행

 --%>

<script>
	$(function(){
		
		let selectedValue = '${search_field}';
		
		if(selectedValue != '-1'){
			$("#viewcount").val(selectedValue);
		}else{
			selectedValue = 0;
		}
		
		
		const message =  ["아이디","이름","나이","남 또는 여"];
		$("input").attr("placeholder" , message[selectedValue] + " 입력하세요");
		
		
		$("button").click(function(){
			if($("input").val() == ""){
				alert("검색어를 입력하세요");
				$("input").focus();
				return false;
			}
			
			const word = $(".input-group input").val();
			
			if(selectedValue == 2){
				const pattern = /^[0-9]{2}$/;
				if(!pattern.test(word)){
					alert("나이는 형식에 맞게 입력하세요(두자리 숫자)");
					return false;
				}
				
			}else if(selectedValue == 3){
				if(word != "남" && word != "여"){
					alert("남 또는 여를 입력하세요");
					return false;
				}
			
			}
				
			
		})
		
		
		
		$("#viewcount").change(function(){
			selectedValue = $(this).val();
			$("input").val();
			$("input").attr("placeholder" , message[selectedValue] + " 입력하세요");
			
		})
		
		
		$("tr > td:nth-child(3) > a").click(function(){
			const answer = confirm("정말 삭제하시겠습니까?");
			if( !answer ){
				event.preventDefault();
			}
	
			
		})
		
		
		
		
		
		
	})
</script>

<style>
	table caption {
	caption-side: top;
	text-align: center
}

h1 {
	text-align: center
}

li .gray {
	color: gray;
}

body>div>table>tbody>tr>td:last-child>a {
	color: red
}

form {
	margin: 0 auto;
	width: 80%;
	text-align: center
}

select {
	color: #495057;
	background-color: #fff;
	background-clip: padding-box;
	border: 1px solid #ced4da;
	border-radius: .25rem;
	transition: border-color .15s ease-in-out, box-shadow .15s ease-in-out;
	outline: none;
}

.container {
	width: 60%
}

td:nth-child(1) {
	width: 33%
}

.input-group {
	margin-bottom: 3em
}
</style>

</head>
	
<body>
	

	<div class="container">
		
		<form action="memberList.net" method="post">
			<div class="input-group">
				<select id="viewcount" name="search_field">
					<option value="0" selected>아이디</option>
					<option value="1" >이름</option>
					<option value="2" >나이</option>
					<option value="3" >성별</option>
				</select>
					<input name="search_word" type="text" class="form-control"
					 placeholder="endet id" value="${ search_word}">
					 <button class='btn btn-primary' type="submit">검색</button>
			</div>
		</form>
		
			<%-- 회원이 있을떄 --%>
			<c:if test="${listcount > 0 }">			
				<table  class='table table-striped'>
					<caption style='font-weight:bold'>회원 목록</caption>
					<thead>
						<tr>
							<th colspan="2">MVC게시판 - 회원 정보 list</th>
							<th ><span>회원 수 : ${listcount}</span></th>
						</tr>
						<tr>
							<th>아이디</th>
							<th>이름</th>
							<th>삭제</th>
			
						</tr>
					</thead>
					
					<tbody>
					
						<c:forEach var="m" items="${totallist }">
							<tr>
								<td><a href="memberInfo.net?id=${m.id }">${m.id }</a></td>
								<td>${m.name }</td>
								<td><a href="memberDelete.net?id=${m.id }">삭제</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				
				<div class="center-block">
					<ul class="pagination justify-content-center">
						<c:if test="${page <= 1 }">
							<li class="page-item">
								<a class="page-link gray">이전 &nbsp;</a>
							</li>
						</c:if>
						<c:if test="${page > 1 }">
							<li class="page-item">
								<a href="memberList.net?page=${page-1 }&search_field=${search_field}&search_word=${search_word}" 
								class="page-link">이전 &nbsp;</a>
							</li>
						</c:if>
						
						<c:forEach var="a" begin="${startpage }" end="${endpage}">
							<c:if test="${a == page}">
								<li class="page-item active">
									<a class="page-link ">${a}</a>
								</li>
							</c:if>
							
							<c:if test="${a != page}">
								<c:url var="go" value="memberList.net">
									<c:param name="search_field" value="${search_field }"/>
									<c:param name="search_word" value="${search_word}"/>
									<c:param name="page" value="${a }"/>
								</c:url>
								<li class="page-item">
									<a href="${go}" class="page-link ">${a}</a>
								</li>
							</c:if>
						
						</c:forEach>
						
						
						<c:if test="${page >= maxpage }">
							<li class="page-item">
								<a class="page-link gray"> &nbsp; 다음</a>
							</li>
						</c:if>
						<c:if test="${page < maxpage  }">
							<c:url var="next" value="memberList.net">
								<c:param name="search_field" value="${search_field }"/>
								<c:param name="search_word" value="${search_word}"/>
								<c:param name="page" value="${page+1 }"/>
							</c:url>
							<li class="page-item">
								<a href="${next }"  class="page-link"> &nbsp; 다음</a>
							</li>
						</c:if>
						
					</ul>
				</div>
				
			</c:if><%-- 회원이 있을떄 --%>
			
			<%-- 회원이 없을 떄 --%>
			<c:if test="${listcount == 0 && empty search_word }">
				<h1>등록된 회원이 없습니다</h1>
			</c:if>
			
			<c:if test="${listcount == 0 && !empty search_word }">
				<h1>검색 결과가 없습니다</h1>
			</c:if>
		
		
	</div>
	

</body>
