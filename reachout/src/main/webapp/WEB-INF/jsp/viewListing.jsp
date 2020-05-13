<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>ReachOut | ${ListingObj.getListingType().getName()}</title>
<%@ include file="/components/stylesheets.jsp"%>
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>
</head>

<body>
	<div class="container-fluid">
		<%@ include file="/components/topHeader.jsp"%>
		<%@ include file="/components/navbar.jsp"%>
		<div class="row profile-row">
			<div class="col-sm-3"></div>
			<div class="col-md-6">
				<div class="card">
					<h2><strong>${ListingObj.getTitle()}</strong></h2>
					<p class="card-text">${ListingObj.getDescription()}</p>
					<hr>
					<p class="card-text">${ListingObj.getCity()}, ${ListingObj.getCounty()}</p>
					<hr>
					<p class="card-text">Status: ${ListingObj.getStatus()}</p>

					<c:choose>
						<c:when test="${enableButton}">
							<div class="row">

								<div class="col-lg-6">
									<!-- Button to trigger modal -->
									<button class="btn btn-info btn-block" data-toggle="modal" data-target="#modalForm">
										<span class="fa fa-comments-o"></span> Message User</button>
								</div>

								<div class="col-lg-6">
									<form action="" method="POST">
										<sec:csrfInput />
										<input type="hidden" id="action" name="action" value="accept" />
										<input type="hidden" id="listingType" name="listingType"
											value="${ListingObj.getListingType().getName()}" /> <input
											type="hidden" id="listingID" name="listingID"
											value="${ListingObj.getId()}" />
										<button class="btn btn-success btn-block">
											<span class="fa fa-handshake-o"></span> Offer Your Help
										</button>
									</form>
								</div>

							</div>
						</c:when>
					</c:choose>

					<c:choose>
						<c:when test="${isOwner == true}">
							<div class="row">
								<div class="col-lg-4"></div>
								<div class="col-lg-4">
									<form action="editListing" method="GET">
										<input type="hidden" id="action" name="action" value="accept" />
										<input type="hidden" id="listingID" name="listingID"
											value="${ListingObj.getId()}" />
										<button class="btn btn-success btn-block">
											<span class="fa fa-info"></span> Edit Listing
										</button>
									</form>
								</div>
							</div>
						</c:when>
					</c:choose>
				</div>
				<div class="col-sm-3"></div>
			</div>


			<!-- Modal -->
			<div class="modal fade" id="modalForm" role="dialog">
				<div class="modal-dialog">
					<div class="modal-content">
						<!-- Modal Header -->
						<div class="modal-header">
							<h4 class="modal-title" id="myModalLabel">Message User</h4>
							<button type="button" class="close" data-dismiss="modal">
								<span aria-hidden="true">&times;</span> <span class="sr-only">Close</span>
							</button>
						</div>

						<!-- Modal Body -->
						<div class="modal-body">
							<p class="statusMsg"></p>
							<form role="form">
								<div class="form-group">
									<textarea class="form-control" id="inputMessage"
										placeholder="Please enter your message to the user. Once you press send, you will see it appear on your My Messages page."></textarea>
								</div>
								<input type="hidden" id="targetID" name="targetID" value="${ListingObj.getUserId()}">
							</form>
						</div>

						<!-- Modal Footer -->
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
							<button type="button" class="btn btn-info submitBtn" onclick="submitContactForm()">SEND</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script>
	// ajax function to send message. Targets the SendUserMessage class
	function submitContactForm() {
		var targetID = $('#targetID').val();
		var message = $('#inputMessage').val();
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		if (message.trim() == '') {
			alert('Please enter your message.');
			$('#inputMessage').focus();
			return false;
		} else {
			console.log("sending message id: " + targetID + " message: "
					+ message);
			$
					.ajax({
						type : 'POST',
						url : 'SendUserMessage',
						data : 'contactFrmSubmit=1&message=' + message
								+ '&targetID=' + targetID,
						beforeSend : function(xhr) {
							$('.submitBtn').attr("disabled", "disabled");
							$('.modal-body').css('opacity', '.5');
							xhr.setRequestHeader(header, token);
						},
						success : function(msg) {
							if (msg == 'SUCCESS') {
								$('#inputMessage').val('');
								$('.statusMsg')
										.html(
												'<span style="color:green;">Message Sent.</p>');
							} else {
								$('.statusMsg')
										.html(
												'<span style="color:red;">Some problem occurred, please try again.</span>');
							}
							$('.submitBtn').removeAttr("disabled");
							$('.modal-body').css('opacity', '');
							$('.statusMsg').html('');
							$('#modalForm').modal('hide');
							$('body').removeClass('modal-open');
							$('.modal-backdrop').remove();
						}
					});
		}
	}
</script>
</html>
