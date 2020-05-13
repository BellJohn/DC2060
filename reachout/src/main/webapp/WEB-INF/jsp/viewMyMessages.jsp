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

		<!-- Title Header -->
		<div class="row listing-page">
			<div class="col-lg-1"></div>
			<div class="col-lg-10">
				<h1>My Messages</h1>
			</div>
			<div class="col-lg-1"></div>
		</div>

		<div class="row">
		<div class="col-lg-1"></div>
			<div class="col-lg-10">

		<div id="messageDisplay" onload="getFreshData()"></div>
		<c:choose>
			<c:when test="${conversations.size() > 0}">
				<div class="row">
					<div class="col-lg-3"></div>
					<div class="col-lg-9">
						<div class="card" style="display: inline-flex; float: inline-end; visibility: hidden; width: 100%;">
							<div id="sendMSGBox" class="input-group" style="visibility: hidden">
								<input type="text" class="form-control" placeholder="Send Message"
									aria-label="Send Message" aria-describedby="button-sendMSG"
									id="inputMessage" />
								<div class="input-group-append">
									<button class="btn btn-outline-secondary" type="button" id="button-sendMSG" onclick="sendMessage()">Send</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</c:when>
		</c:choose>

		</div>
	<div class="col-lg-1"></div>

	</div>

	<script>
		var activeUID;
		getFreshData(); // This will run on page load
		setInterval(function() {
			getFreshData() // this will run after every 5 seconds
		}, 5000);

		function getFreshData() {
			$
					.ajax({
						type : 'GET',
						url : 'viewMyMessages?targetID=' + activeUID,
						complete : function(resp) {
							var obj = jQuery.parseJSON(resp
									.getResponseHeader('conversationstring'));

							// Test to see how many conversations we have to build.
							// If there are none, display a relevant message instead.
							if (obj.length == 0) {
								content = "<div class=\"row\">";
								content += "<div class=\"col-lg-1\"></div>";
								content += "<div class=\"col-lg-10\">";
								content += "<div class=\"alert alert-primary\" role=\"alert\" style=\"text-align: center;\">";
								content += "<p>Page looking a bit empty? Try striking up a conversation with another user first!</p>";
								content += "<p>Why not start by seeing if there is a Request out there which you can help with?</p>";
								content += "</div>";
								content += "<div class=\"col-lg-1\"></div>";
								content += "</div>";
								// Populate with the new data
								var displayDiv = document
										.getElementById("messageDisplay");
								displayDiv.innerHTML = content;
								return;
							}

							// get last selected user and do the magic
							// For each Conversation
							var tabStyle = "style=\"overflow-y: scroll; max-width: 30%; float: left; min-height: 500px;\"";
							var content = "<div class=\"tab\" " + tabStyle + ">";
							for (var i = 0; i < obj.length; i++) {

								if (activeUID == obj[i].userOther) {
									content += "<button class=\"tablinks active\"  onclick=\"openConvo(event, "
											+ obj[i].userOther
											+ ")\">"
											+ obj[i].otherUserName
											+ "</button>";
								} else {
									// Add the User tab button
									content += "<button class=\"tablinks\"  onclick=\"openConvo(event, "
											+ obj[i].userOther
											+ ")\">"
											+ obj[i].otherUserName
											+ "</button>";
								}
							}
							content += "</div>";

							var tabContentStyleVisible = "style=\"display: flex; flex-direction: column; block; max-width: 80%; min-height: 500px; max-height: 500px;\"";

							var tabContentStyleHidden = "style=\"; flex-direction: column; display: none; max-width: 80%; min-height: 500px; max-height: 500px;\"";
							var activeElement = "";
							// iterate again for each of the tab contents
							for (var i = 0; i < obj.length; i++) {
								// For each message in that conversation
								if (activeUID == obj[i].userOther) {
									content += "<div id=\"" + obj[i].userOther + "\" class=\"tabcontent active\" " + tabContentStyleVisible +">";
									activeElement = obj[i].userOther;
								} else {
									content += "<div id=\"" + obj[i].userOther + "\" class=\"tabcontent\" " + tabContentStyleHidden +">";
								}

								content += "<div id=\"scrollBarConvo"+obj[i].userOther+"\" style=\"overflow-y: scroll;\">";
								for (var j = 0; j < obj[i].allIMsAsList.length; j++) {
								
									if(obj[i].userBrowsing == obj[i].allIMsAsList[j].origin){
										// Create a message element sent by the user browsing
										// Add the messages to the tab window
										content += "<div class=\"card message-card\" style=\"background-color: #AAFFDF;\">";
										content += "<div class=\"row\">";
										//open card body
										content += "<div class=\"col-lg-9\">";
										content += "<p style=\"margin-bottom: 0px;\">"
												+ obj[i].allIMsAsList[j].message
												+ "</p>";
										//close card body
										content += "</div>";
										content += "<div class=\"col-lg-3\" style=\"border-left: 2px solid grey;\">";
										content += "<img src=\"images/no-profile-pic.png\" class=\"avatar\" style=\"vertical-align: middle; width: 50px; height: 50px; border-radius: 50%;\"/>";
										content += "<h5>You</h5>";
										content += "<p style=\"font-size: small;margin-bottom: 0px;\">"
												+ obj[i].allIMsAsList[j].prettyPrintDate
												+ "</p>";
										//close card header
										content += "</div>";
										//close card
										content += "</div>";
										content += "</div>";
									}
									else{
									// Create a message element sent by the other party in the conversation
										// Add the messages to the tab window
										content += "<div class=\"card message-card\" style=\"background-color: #c3edea;\">";
										content += "<div class=\"row\">";
										content += "<div class=\"col-lg-3\" style=\"border-right: 2px solid grey;\">";
										content += "<img src=\"images/no-profile-pic.png\" class=\"avatar\" style=\"vertical-align: middle; width: 50px; height: 50px; border-radius: 50%;\"/>";
										content += "<h5>" + obj[i].otherUserName +"</h5>";
										content += "<p style=\"font-size: small;margin-bottom: 0px;\">"
												+ obj[i].allIMsAsList[j].prettyPrintDate
												+ "</p>";
										//close card header
										content += "</div>";
										//open card body
										content += "<div class=\"col-lg-9\">";
										content += "<p style=\"margin-bottom: 0px;\">"
												+ obj[i].allIMsAsList[j].message
												+ "</p>";
										//close card body
										content += "</div>";
										//close card
										content += "</div>";
										content += "</div>";
									}
									
								}

								// close the overflow y div
								content += "</div>";

								// Add the message box & content
								content += "</div>";
							}

							// Populate with the new data
							var displayDiv = document
									.getElementById("messageDisplay");
							displayDiv.innerHTML = content;

							// Set the scroll bar to the bottom
							dropScrollBarToBottom(activeElement);
						}
					});
		}

		function dropScrollBarToBottom(uid) {
			// Set the scroll bar to the bottom
			var scrollBox = "scrollBarConvo" + uid;
			var element = document.getElementById(scrollBox);
			element.scrollTop = element.scrollHeight;
		}

		function openConvo(evt, uid) {

			activeUID = uid;
			// Enable the messaging button and make the div visible
			var buttonID = "button-sendMSG";
			var sendMsgBox = "sendMSGBox";
			document.getElementById(buttonID).disabled = false;
			document.getElementById(sendMsgBox).style.visibility = 'visible';

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
			document.getElementById(uid).style.display = "flex";
			evt.currentTarget.className += " active";

			dropScrollBarToBottom(uid);
		}

		// Attempts to send the message to the InternalMessageHandler
		// On success, the IM data is refreshed to include the new message
		function sendMessage() {
			var message = $('#inputMessage').val();
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			if (message.trim() == '') {
				alert('Please enter your message.');
				$('#inputMessage').focus();
				return false;
			} else {
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
						}
					},
					complete : function(resp) {
						activeUID = resp.getResponseHeader('targetID');
						getFreshData();
					}
				});
			}
		}

		$(document).ready(function(){
			$('#inputMessage').keypress(function(e){
			if(e.keyCode==13)
				$('#button-sendMSG').click();
			});
		});
	</script>
</body>

</html>