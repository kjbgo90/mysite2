<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link href="${pageContext.request.contextPath }/assets/css/mysite.css" rel="stylesheet" type="text/css">
	<link href="${pageContext.request.contextPath }/assets/css/guestbook.css" rel="stylesheet" type="text/css">
	<title>Mysite</title>
</head>
<body>
	<div id="container">
		<!-- header -->
		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		
		<!-- /navigation -->
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
		
		<div id="content">
			<div id="c_box">
				<div id="guestbook">
					<h2>방명록</h2>
					
					<form id="addform" action="${pageContext.request.contextPath }/guestbook" method="post">
						<input type="hidden" name="action" value="insert" >
						<table>
							<tr>
								<c:choose>
									<c:when test="${empty authUser}">
										<!-- 로그인 전 -->
										<td>이름</td>
										<td><input type="text" name="name"></td>
									</c:when>
									<c:otherwise>
										<!-- 로그인 후 -->
										<td>이름</td>
										<td><input type="text" name="name" value="${authUser.name}"></td>
									</c:otherwise>
								</c:choose>
								<td>비밀번호</td>
								<td><input type="password" name="password"></td>
							</tr>
							<tr>
								<td colspan=4><textarea name="content" cols="75" rows="8"></textarea></td>
							</tr>
							<tr>
								<td colspan=4 align=right><input type="submit" VALUE=" 확인 "></td>
							</tr>
						</table>
					</form>
					<c:forEach items="${guestList}" var="vo">
						<table width=510 border=1>
							<tr>
								<td>[${vo.no}]</td>
								<td>${vo.name}</td>
								<td>${vo.reg_date}</td>
								<td><a href="./guestbook?action=deleteform&no=${vo.no}">삭제</a></td>
							</tr>
							<tr>
								<td colspan="4">${vo.content}</td>
							</tr>
						</table>
			    		<br>
		    		</c:forEach>
				</div><!-- /guestbook -->
			</div><!-- /c_box -->
		</div><!-- /content -->
			
			
			
		<!-- /footer -->
		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
	</div><!-- /container -->
</body>
</html>

