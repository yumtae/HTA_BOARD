<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
<title>MVC 게시판 - view</title>
<jsp:include page="header.jsp"/>
<script src="${pageContext.request.contextPath}/resources/js/view.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/view.css" type="text/css">

	<script>
		let result = "${result}";
		if(result == 'passFail'){
			alert("비밀번호가 일치하지 않습니다");
		}
		$(function(){
			$("form[action=delete]").submit(function(){
				if($("#board_pass").val() ==''){
					alert("비밀번호를 입력하세요");
					$("#board_pass").focus();
					return false;
				}
			
			})
		})
		
	</script>

</head>
<body>
  
   <!-- js에서 처리 <input type="hidden" id="loginid" value="${id}" name="loginid">  -->
   <div class="container">
      <table class="table table-striped">
         <tr>
            <th colspan="2">MVC 게시판-view페이지</th>
         </tr>
         <tr>
            <td><div>글쓴이</div></td>
            <td><div>${boarddata.BOARD_NAME}</div></td>
         </tr>
         <tr>
            <td><div>제목</div></td>
            <td><c:out value="${boarddata.BOARD_SUBJECT}"/></td>
         </tr>
         <tr>
            <td><div>내용</div></td>
            <td style="padding-right: 0px">
               <textarea class="form-control"
                       rows="5" readOnly>${boarddata.BOARD_CONTENT}</textarea>
            </td>
         </tr>
         
         <c:if test="${boarddata.BOARD_RE_LEV==0}">
            <%-- 원문글인 경우에만 첨부파일을 추가 할 수 있습니다. --%>
            <tr>
               <td><div>첨부파일</div></td>
               
               <%-- 파일을 첨부한 경우 --%>
               <c:if test="${!empty boarddata.BOARD_FILE}">
                  <td><img src="${pageContext.request.contextPath}/resources/image/down.png" width="10px">
                   	<form method="post" action="down" style="height:0px">
                   		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                   		<input type="hidden" value="${boarddata.BOARD_FILE}" name="filename">
                   		<input type="hidden" value="${boarddata.BOARD_ORIGINAL}" name="original">
                   		<input type="submit" value="${boarddata.BOARD_ORIGINAL}" >
                   	</form>
                   
                   </td>
               </c:if>
               
               <%-- 파일을 첨부하지 않은 경우 --%>
               <c:if test="${empty boarddata.BOARD_FILE}">
                  <td></td>
               </c:if>
            </tr>
         </c:if>
         
         <tr>
            <td colspan="2" class="center">
            	<button class="btn btn-primary">댓글</button>
            	<span id="count">${count }</span>
            	<sec:authentication property="principal" var="pinfo"/>
            	<sec:authorize access="isAuthenticated()">
            	
               <c:if test="${boarddata.BOARD_NAME== pinfo.username || pinfo.username == 'admin'}">
                     <a href="modifyView?num=${boarddata.BOARD_NUM}">
                        <button class="btn btn-warning">수정</button>
                     </a>
             
                     <a href="#">
                        <button class="btn btn-danger" data-toggle="modal"
                           data-target="#myModal">삭제</button>
                     </a>
                  </c:if>
                 </sec:authorize>
                   <a href="replyView?num=${boarddata.BOARD_NUM}">
                       <button class="btn btn-info">답변</button>
                   </a>
                    <a href="list">
                       <button class="btn btn-success">목록</button>
                    </a>
                    
              </td>
            </tr>
      </table>
      
      
      	<div class='modal' id="myModal" >
      		<div class="modal-dialog">
	      		<div class="modal-content">
	      			<div class="modal-body">
	      				<form name="deleteForm" action="delete" method="post">
	      					<!-- ${param.num} 또는 ${board_data.BOARD_NUM} -->
	      					<input type="hidden" name ="num" value="${param.num}" id="board_num">
	      					<div class="form-group">
	      						<label for ="pwd">비밀번호</label>
	      						<input type="password" class="form-control" placeholder="Enter password"
	      								name="BOARD_PASS" id="board_pass">
	      					</div>
	      					   <button type="submit" class="btn btn-primary">전송</button>
	      					   <button type="button" class="btn btn-danger" data-dismiss="modal">취소</button>
	      					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
	      				</form>
	      			</div>
	      		</div>
	      	</div>
      	</div><%--modal --%>
      
      
      
      	<div id="comment">
	      <button id='comment_limit_text' class="btn btn-info float-left">총 50자까지 가능합니다.</button>
      	  <button id="write" class="btn btn-info float-right">등록</button>
      	  <textarea rows=3 class="form-control" id="content" maxLength="50"></textarea>
      	  <table class="table table-striped">
      	  	<thead>
      	  		<tr><td>아이디</td><td>내용</td><td>날짜</td></tr>
      	  	</thead>
      	  	<tbody>
      	  	
      	  	</tbody>
      	  	
      	  </table>
      	  	<div id="message"></div>
      	</div><%--comment --%>
      
      
      
      
      
   </div>
</body>
</html>