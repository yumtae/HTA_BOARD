<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix ="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<script src="https://cdn.jsdelivr.net/npm/jquery@3.6.1/dist/jquery.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
  
  <meta name="_csrf" content="${_csrf.token}">
  <meta name="_csrf_header" content="${_csrf.headerName}">
  
<meta charset="UTF-8">
<style>
#logout{background:none;margin:0;border: 0;;width:auto}
nav.navbar {
	justify-content : flex-end; /*오른쪽 정렬*/
}

.dropdown-menu{
min-width:0rem
}

/*nav 색상*/
.navbar{
	background:#096988;
	margin-bottom:3em;
	padding-right:3em;
}

.navbar-dark .navbar-nav . nav-link{
	color: #fff;
}

textarea{
	resize:none;
}

</style>


<script>
	$(function(){
			$("#logout").click(function(){
				event.preventDefault();
				$("form[name=logout]").submit();
			})
		
	})
</script>

<!-- isAnonymous()  : 익명 사용자인 경우 로그인 페이지로 이동하도록 합니다.-->
<sec:authorize access="isAnonymous()">
	<script>
		location.href="${pageContext.request.servletContext }/member/login";
	</script>
</sec:authorize>


<nav class="navbar navbar-expand-sm right-block navbar-dark">
	
	<!-- Brand
	
		1. isAuthenticated()는 인증한 사용자인 경우 아래의 코드를 실행합니다.
		2. <sec:authentication property="principal" var="pinfo"/>
							인증된 사용자의 정보를 가져옵니다.
			pinfo.username은 아이디값을 ,pinfo.password는 비밀번호를 알 수 있습니다	
			
	 -->

	<ul class='navbar-nav'>
	
		<sec:authorize access="isAuthenticated()">
			<sec:authentication property="principal" var="pinfo"/>
			<li class='nav-item'>
				<form id="logout" action="${pageContext.request.contextPath}/member/logout" method="post" style="margin-bottom:0px" name="logout">
					<a class="nav-link" href="#" id="logout">
						<span id="loginid">${pinfo.username }</span>님(로그아웃)
					</a>
					<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
				
				</form>
			</li>
		</sec:authorize>
		
			<li class='nav-item'>
				<a class="nav-link" href="${pageContext.request.contextPath}/member/update">정보수정</a>
			</li>
			
			<c:if test="${pinfo.username=='admin' }">
					<li class='nav-item dropdown'>
						<a class="nav-link dropdown-toggle" href="#" id="navbardrop"  data-toggle="dropdown">관리자</a>
						<div class='dropdown-menu' >
							<a class="dropdown-item" href="${pageContext.request.contextPath}/member/list">회원정보</a>
							<a class="dropdown-item" href="${pageContext.request.contextPath}/board/list">게시판</a>
						</div>
					</li>
			</c:if>
			
	</ul>

</nav>






