<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>ReachOut | My Messages</title>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/components/stylesheets.jsp"%>
</head>

<body>
	<div class="container-fluid">
		<%@ include file="/components/navbar.jsp"%>
		<div class="jumbotron">
			<div class="row">My Messages</div>
		</div>
		<div class="tab" style="overflow-y: scroll;">
			<c:forEach items="${conversations}" var="convo">
				<button class="tablinks"
					onclick="openMessage(event, '${convo.getUserOther()}')">${convo.getOtherUserName()}</button>
			</c:forEach>
		</div>
		<div id="defaultTabContent" class="tabcontent active"
			style="display: block">
			<h6>Pick a user to see your conversation history</h6>
		</div>
		<c:forEach items="${conversations}" var="convo">
			<div id="${convo.getUserOther()}" class="tabcontent"
				style="overflow-y: scroll; display: none;">

				<c:forEach items="${convo.getAllIMsAsList()}" var="message">
					<div>
						<h6>${message.getPrettyPrintDate()}</h6>
						<p>${message.getMessage()}</p>
					</div>
				</c:forEach>
			</div>
		</c:forEach>
	</div>

	<script>
		
		function openMessage(evt, uid) {
			// Declare all variables
			var i, tabcontent, tablinks;

			// Get all elements with class="tabcontent" and hide them
			tabcontent = document.getElementsByClassName("tabcontent");
			for (i = 0; i < tabcontent.length; i++) {
				tabcontent[i].style.display = "none";
			}

			// Get all elements with class="tablinks" and remove the class "active"
			tablinks = document.getElementsByClassName("tablinks");
			for (i = 0; i < tablinks.length; i++) {
				tablinks[i].className = tablinks[i].className.replace(
						" active", "");
			}

			// Show the current tab, and add an "active" class to the link that opened the tab
			document.getElementById(uid).style.display = "block";
			evt.currentTarget.className += " active";
		}
	</script>
</body>

</html>