<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>ReachOut | My Messages</title>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/components/stylesheets.jsp"%>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
</head>

<body>
	<div class="container-fluid">
		<%@ include file="/components/navbar.jsp"%>
		<div class="jumbotron">
			<div class="row">My Messages</div>
		</div>
		<div class="tab" style="overflow-y: scroll;">
			<c:forEach items="${conversations}" var="convo">
				<c:choose>
					<c:when test="${previousUser==convo.getUserOther()}">
						<button class="tablinks active"
							onclick="openConvo(event, '${convo.getUserOther()}')">${convo.getOtherUserName()}</button>
					</c:when>
					<c:otherwise>
						<button class="tablinks"
							onclick="openConvo(event, '${convo.getUserOther()}')">${convo.getOtherUserName()}</button>
					</c:otherwise>
				</c:choose>

			</c:forEach>
		</div>

		<!--  If we have received no previous user then just populate with default open tab -->
		<c:if test="${empty previousUser}">
			<div id="defaultTabContent" class="tabcontent active"
				style="display: block">
				<h6>Pick a user to see your conversation history</h6>
			</div>
		</c:if>
		<c:forEach items="${conversations}" var="convo">
			<c:choose>
				<c:when test="${previousUser==convo.getUserOther()}">
					<div id="${convo.getUserOther()}" class="tabcontent active"
						style="overflow-y: scroll; display: block;">
				</c:when>
				<c:otherwise>
					<div id="${convo.getUserOther()}" class="tabcontent"
						style="overflow-y: scroll; display: none;">
				</c:otherwise>
			</c:choose>

			<c:forEach items="${convo.getAllIMsAsList()}" var="message">
				<div>
					<h6>${message.getPrettyPrintDate()}</h6>
					<p>${message.getMessage()}</p>
				</div>
			</c:forEach>
	</div>
	</c:forEach>
	<div id="sendMSGBox" class="input-group mb-3"
		style="visibility: hidden">
		<input type="text" class="form-control" placeholder="Send Message"
			aria-label="Send Message" aria-describedby="button-sendMSG"
			id="inputMessage">
		<div class="input-group-append">
			<button class="btn btn-outline-secondary" type="button"
				id="button-sendMSG" onclick="sendMessage()" disabled="disabled">Send</button>
		</div>
	</div>
	</div>

	<script>
		var activeUID;

		function openConvo(evt, uid) {
			activeUID = uid;
			// Enable the messaging button and make the div visible
			document.getElementById("button-sendMSG").disabled = false;
			document.getElementById("sendMSGBox").style.visibility = 'visible'

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

		function sendMessage() {
			var message = $('#inputMessage').val();
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			if (message.trim() == '') {
				alert('Please enter your message.');
				$('#inputMessage').focus();
				return false;
			} else {
				console.log("sending message id: " + activeUID + " message: "
						+ message);
				$.ajax({
					type : 'POST',
					url : 'SendUserMessage',
					data : 'message=' + message + '&targetID=' + activeUID,
					beforeSend : function(xhr) {
						$('.submitBtn').attr("disabled", "disabled");
						xhr.setRequestHeader(header, token);
					},
					success : function(msg) {
						if (msg == 'SUCCESS') {
							$('#inputMessage').val('');

							$('.submitBtn').removeAttr("disabled");
							// TODO Replace this whole section with a jQuery call on page load to populate the page with 
							// data rather than returning it in the primary GET request and populating through the jsp
							// Consider https://stackoverflow.com/questions/25446628/ajax-jquery-refresh-div-every-5-seconds
							// Trigger reload of page with the current user active
							window.location.href = 'viewMyMessages'
									+ '?targetID=' + activeUID;
						}
					}
				});
			}
		}
	</script>
</body>

</html>