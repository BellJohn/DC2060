<!DOCTYPE html>
<html lang="en-GB">
	<head>
		<title>ReachOut | Blog</title>
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

			<c:forEach items="${blogPosts}" var="post">

				<div class="card mb-3 blogpost">
					<div class="card-body">
						<h5 class="card-title">${post.title}</h5>
						<p class="card-text">${post.content}</p>
						<p class="card-text"><small class="text-muted">${post.author}, ${post.date}</small></p>
					</div>
				</div>

			</c:forEach>
			
		</div>
	</body>

</html>