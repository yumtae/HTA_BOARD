<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix ="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp"/>
<html>
<head>

<style>
	select.form-control{
		width:auto;
		margin-bottom:2em;
		display:inline-block
	}
	
	.rows{
		text-align:center
	}
	
	.gray{color:gray}

	body > div > table > thead tr:nth-child(2) > th:nth-child(1){width:8%}
	body > div > table > thead tr:nth-child(2) > th:nth-child(2){width:50%}
	body > div > table > thead tr:nth-child(2) > th:nth-child(3){width:14%}
	body > div > table > thead tr:nth-child(2) > th:nth-child(4){width:17%}
	body > div > table > thead tr:nth-child(2) > th:nth-child(5){width:11%}

</style>

<script>
	const result = "${result}";
	if(result== 'deleteSuccess'){
		alert('삭제 성공 입니다.');
	}else if(result == 'updateSuccess'){
		alert('회원 정보가 수정되었습니다.')
	}
	
	
</script>



<script src="../resources/js/list.js"></script>
</head>
<title>MVC 게시판</title>
<body>


	<div class="container">
			<%-- 게시글이 있을떄 --%>
			<c:if test="${listcount > 0 }">
				<div class='rows'>
					<span>줄보기</span>
					<select class="form-control" id="viewcount">
						<option value="1">1</option>
						<option value="3">3</option>
						<option value="5">5</option>
						<option value="7">7</option>
						<option value="10" selected>10</option>
					
					</select>
				</div>
			
				<table  class='table table-striped'>
					<thead>
						<tr>
							<th colspan="3">MVC게시판 - list</th>
							<th colspan="2"><span>글 개수 : ${listcount}</span></th>
						</tr>
						<tr>
							<th><div>번호</div></th>
							<th><div>제목</div></th>
							<th><div>작성자</div></th>
							<th><div>날짜</div></th>
							<th><div>조회수</div></th>
						</tr>
					</thead>
					
					<tbody>
						<c:set var="num" value="${listcount-(page-1)*limit }"/>
						<c:forEach var="b" items="${boardlist }">
							<tr>
								<td><%-- 번호 --%>
									<c:out value="${num }"/><%-- num출력 --%>
									<c:set var="num" value = "${num-1 }" /><%-- num-1계산 --%>
								</td><%-- 번호 끝--%>
								
								<td><%-- 제목 --%>
									<div>
					
										<%-- 답변글 제목아펭 여백 처리 부분
											board_re_lev, board_num, board_subject,
											board_name, board_date, board_readcount : property 이름--%>
										<c:if test="${b.BOARD_RE_LEV !=0 }"> <%-- 답글인 경우--%>
											<c:forEach var="a" begin="0" end="${b.BOARD_RE_LEV*2 }" step="1">
												&nbsp;
											</c:forEach>
											<img src="${pageContext.request.contextPath}/resources/image/line.gif">
										</c:if>
								
										<c:if test="${b.BOARD_RE_LEV==0 }"> <%-- 원문인경우--%>
											&nbsp;
										</c:if>
								
										<a href="detail?num=${b.BOARD_NUM }">
											<!-- escapeXml="true" : HTML 태그를 화면에  -->
											<c:out value="${b.BOARD_SUBJECT}" escapeXml="true" />
										</a>[<c:out value="${b.cnt }"/>]
								
									</div>
								</td><%-- 제목끝 --%>
								<td><div>${b.BOARD_NAME }</div></td>
								<td><div>${b.BOARD_DATE }</div></td>
								<td><div>${b.BOARD_READCOUNT }</div></td>
								
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
								<a href="BoardList.bo?page=${page-1 }" class="page-link">이전 &nbsp;</a>
							</li>
						</c:if>
						
						<c:forEach var="a" begin="${startpage }" end="${endpage}">
							<c:if test="${a == page}">
								<li class="page-item active">
									<a class="page-link ">${a}</a>
								</li>
							</c:if>
							
							<c:if test="${a != page}">
								<li class="page-item">
									<a href="BoardList.bo?page=${a}" class="page-link ">${a}</a>
								</li>
							</c:if>
						
						</c:forEach>
						
						
						<c:if test="${page >= maxpage }">
							<li class="page-item">
								<a class="page-link gray"> &nbsp; 다음</a>
							</li>
						</c:if>
						<c:if test="${page < maxpage  }">
							<li class="page-item">
								<a href="BoardList.bo?page=${page+1 }" class="page-link"> &nbsp; 다음</a>
							</li>
						</c:if>
						
					</ul>
				</div>
				
			</c:if><%-- 게시글이 있을떄 --%>
			
			<%-- 게시글이 없을 떄 --%>
			<c:if test="${listcount == 0 }">
				<h3 style="text-align:center">등록된 글이 없습니다</h3>
			</c:if>
			<%-- 게시글이 없을 떄 --%>
			
			
			<button type="button" class="btn btn-info float-right">글 쓰 기</button>
			
	</div>

</body>




</html>
    
    