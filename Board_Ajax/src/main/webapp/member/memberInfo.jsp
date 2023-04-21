<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ taglib prefix ="c"  uri="http://java.sun.com/jsp/jstl/core" %>

<head>

<title>회원 관리 시스템 관리자 모드(회원 상세 보기)</title>
<jsp:include page="../board/header.jsp"/>

<style>
	table{border:1px solid #ddd ;width:500px;text-align:Center;margin: 0 auto}

</style>

<body>
	

	<div class="container">

				<h1 style='font-weight:bold'>${memberinfo.id} 계정의 정보</h1>
				<table  class='table table-striped'>
					
	
						<tr>
							<td>아이디</td>
							<td>${memberinfo.id }</td>
						</tr>
						
						<tr>
							<td>비밀번호</td>
							<td>${memberinfo.password }</td>
						</tr>
						
						<tr>
							<td>이름</td>
							<td>${memberinfo.name }</td>
						</tr>
						
						<tr>
							<td>나이</td>
							<td>${memberinfo.age }</td>
						</tr>
						<tr>
							<td>성별</td>
							<td>${memberinfo.gender }</td>
						</tr>
						<tr>
							<td>이메일 주소</td>
							<td>${memberinfo.email }</td>
						</tr>
						<tr >
							<td colspan=2>
								<a href="memberList.net">리스트로 돌아가기</a>
							</td>
						
						</tr>

				</table>
				
			

		
	</div>
	

</body>
