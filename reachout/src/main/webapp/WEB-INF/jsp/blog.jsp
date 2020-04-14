<!DOCTYPE html>
<html lang="en-GB">
<head>
<title>ReachOut Blog</title>
<%@ include file="/components/stylesheets.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

</head>

<body>
	<div class="container-fluid">
		<%@ include file="/components/topHeader.jsp"%>
		<%@ include file="/components/navbar.jsp"%>
		<div class="jumbotron">
			<h2>ReachOut development blog</h2>
			<p>See how our developers are getting on with the increasing pressures and demand for this type of application. We are motivated to get this app released to the public as soon as we can. </p>
			
		</div>
		<div class="row">
			<div class="col-md-6 col-md-offset-3">

				<c:forEach items="${blogPosts}" var="post">
					<tr>      
						<td>${post.title}</td>
						<td>${post.author}</td>
						<td>${post.content}</td>
					</tr>
				</c:forEach>

			</div>
		</div>
	</div>

</body>
</html>