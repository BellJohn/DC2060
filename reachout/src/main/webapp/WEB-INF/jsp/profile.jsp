<!DOCTYPE html>
<html lang="en-GB">
<head>
<title>ReachOut | Profile</title>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/components/stylesheets.jsp"%>
<meta charset="UTF-8">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js">
	
</script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script>
</head>

<body>
	<div class="container-fluid">
		<%@ include file="/components/navbar.jsp"%>

		<div class="row profile-row">

			<!-- User Profile Card -->
			<div class="col-md-3">
				<div class="card card-bkg">

					<!-- Display Users Name -->
					<h2>
						<strong>${firstName} ${lastName}</strong>
					</h2>

					<!-- Display Users Image -->
					<figure>
						<c:choose>
							<c:when test="${empty profilePic}">
								<img src="images/no-profile-pic.png" alt="NoProfilePic"
									class="rounded-circle">
							</c:when>
							<c:otherwise>
								<img src="${profilePic}" alt="profilePic" class="rounded-circle"
									style="max-width: 40%;">
							</c:otherwise>
						</c:choose>
					</figure>

					<!-- Users Bio and health status (or default values) -->
					<p>
						<c:choose>
							<c:when test="${empty bio}">
							Update your profile below to add a bio.
						</c:when>
							<c:otherwise>
							${bio}
						</c:otherwise>
						</c:choose>
						<br>-<br>
						<c:choose>
							<c:when test="${empty healthStatus}">
							Please Update Profile (below)
						</c:when>
							<c:otherwise>
							${healthStatus}
						</c:otherwise>
						</c:choose>
					</p>

					<hr />

					<!-- Service/Request Info and Create Buttons -->
					<div class="row">
						<div class="col-md-6">
							<h2>
								<strong>${numRequests}</strong>
							</h2>
							<p>
								<small>Request(s) Made</small>
							</p>

							<form action="createRequest">
								<button class="btn btn-success">
									<span class="fa fa-plus-circle"></span> Create New Request
								</button>
							</form>
						</div>

						<div class="col-md-6">
							<h2>
								<strong>${numServices}</strong>
							</h2>
							<p>
								<small>Service(s) Offered</small>
							</p>

							<form action="createService">
								<button class="btn btn-info">
									<span class="fa fa-user"></span> Offer New Service
								</button>
							</form>
						</div>
					</div>

					<!-- Update Profile Button -->
					<a class="btn btn-primary btn-large btn-block update-profile-btn"
						href="updateProfile">Update Profile</a>
				</div>
			</div>

			<!-- Users Help Requests -->
			<div class="col-md-9">
				<div class="request-row">
					<h3>Your Requests</h3>

					<c:if test="${empty liveRequests}">
						<div class="card card-request">
							<h4 class="card-title">No Requests Created</h4>
							<p class="card-text">You can create one using the Create new
								Request button over on the left, or by visiting the Requests
								page from the navigation bar.</p>
						</div>
					</c:if>

					<c:forEach items="${liveRequests}" var="request">

						<div class="card card-request">
							<div class="row">
								<h4 class="card-title col-md-9">${request.getTitle()}</h4>
								<h4 class="card-title col-md-3" style="text-align: right;">${request.getPriority()}</h4>
							</div>
							<h6 class="card-subtitle mb-2 text-muted">${request.getCity()},
								${request.getCounty()}</h6>
							<p class="card-text">${request.getFormattedDescription()}</p>

							<div class="row">
								<div class="col-lg-2">
									<c:choose>
										<c:when test="${request.getStatus().getOrdinal()==0}">
											<button class="btn btn-success btn-block" disabled>OPEN</button>
										</c:when>
										<c:when test="${request.getStatus().getOrdinal()==1}">
											<button class="btn btn-warning btn-block" disabled>PENDING</button>
											<div class="modal fade"
												id="modalForm_R_${acceptRequestIDUserIDMap.get(request.getId())}"
												role="dialog">
												<div class="modal-dialog">
													<div class="modal-content">
														<!-- Modal Header -->
														<div class="modal-header">
															<h4 class="modal-title"
																id="myModalLabel_R_${acceptRequestIDUserIDMap.get(request.getId())}">Message
																${userIDtoUsernameMap.get(acceptRequestIDUserIDMap.get(request.getId()))}</h4>
															<button type="button" class="close" data-dismiss="modal">
																<span aria-hidden="true">&times;</span> <span
																	class="sr-only">Close</span>
															</button>
														</div>

														<!-- Modal Body -->
														<div class="modal-body">
															<p class="statusMsg"></p>
															<form role="form">
																<div class="form-group">
																	<textarea class="form-control"
																		id="inputMessage_R_${acceptRequestIDUserIDMap.get(request.getId())}"
																		placeholder="Please enter your message to the user. Once you press send, you will see it appear on your My Messages page."></textarea>
																</div>

															</form>
														</div>

														<!-- Modal Footer -->
														<div class="modal-footer">
															<button type="button" class="btn btn-default"
																data-dismiss="modal">Cancel</button>
															<button type="button" class="btn btn-info submitBtn"
																onclick="submitContactForm('R_${acceptRequestIDUserIDMap.get(request.getId())}')">SEND</button>
														</div>
													</div>
												</div>
											</div>
										</c:when>
										<c:when test="${request.getStatus().getOrdinal()==2}">
											<button class="btn btn-danger btn-block" disabled>CLOSED</button>
										</c:when>
									</c:choose>
								</div>


								<c:choose>
									<c:when test="${request.getStatus().getOrdinal()==1}">
										<div class="col-lg-4"></div>
										<div class="col-lg-2">
											<!-- Button to trigger modal -->
											<button class="btn btn-warning btn-block" data-toggle="modal"
												data-target="#modalForm_R_${acceptRequestIDUserIDMap.get(request.getId())}">
												<span class="fa fa-comments-o"></span> Message ${userIDtoUsernameMap.get(acceptRequestIDUserIDMap.get(request.getId()))}
											</button>
										</div>
									</c:when>
									<c:otherwise>
										<div class="col-lg-6"></div>
									</c:otherwise>
								</c:choose>

								<div class="col-lg-2">
									<form action="viewListing" method="POST">
										<sec:csrfInput />
										<input type="hidden" id="listingType" name="listingType"
											value="${request.getListingType().getName()}" /> <input
											type="hidden" id="listingID" name="listingID"
											value="${request.getId()}" />
										<button class="btn btn-info btn-block">
											<span class="fa fa-info"></span> View Details
										</button>
									</form>
								</div>
								<div class="col-lg-2">
									<!-- Edit Details Button -->
									<form action="editListing" method="GET">
										<input type="hidden" id="listingID" name="listingID"
											value="${request.getId()}" />
										<button class="btn btn-success btn-block">
											<span class="fa fa-info"></span> Edit Details
										</button>
									</form>
								</div>

							</div>
						</div>

					</c:forEach>
				</div>

				<!-- Users Offered Services -->
				<div class="request-row">
					<h3>Your Offered Services</h3>

					<c:if test="${empty liveServices}">
						<div class="card card-request">
							<h4 class="card-title">No Offered Services</h4>
							<p class="card-text">You can create one using the Offer new
								Service button over on the left, or by visiting the Services
								page from the navigation bar.</p>
						</div>
					</c:if>

					<c:forEach items="${liveServices}" var="service">

						<div class="card card-request">
							<h4 class="card-title">${service.getTitle()}</h4>
							<h6 class="card-subtitle mb-2 text-muted">${service.getCity()},
								${service.getCounty()}</h6>
							<p class="card-text">${service.getFormattedDescription()}</p>

							<div class="row">
								<div class="col-lg-2">
									<c:choose>
										<c:when test="${service.getStatus().getOrdinal()==0}">
											<button class="btn btn-success btn-block" disabled>OPEN</button>
										</c:when>
										<c:when test="${service.getStatus().getOrdinal()==1}">
											<button class="btn btn-warning btn-block" disabled>PENDING</button>
										</c:when>
										<c:when test="${service.getStatus().getOrdinal()==2}">
											<button class="btn btn-danger btn-block" disabled>CLOSED</button>
										</c:when>
									</c:choose>
								</div>

								<div class="col-lg-6"></div>

								<div class="col-lg-2">
									<form action="viewListing" method="POST">
										<sec:csrfInput />
										<input type="hidden" id="listingType" name="listingType"
											value="${service.getListingType().getName()}" /> <input
											type="hidden" id="listingID" name="listingID"
											value="${service.getId()}" />
										<button class="btn btn-info btn-block">
											<span class="fa fa-info"></span> View Details
										</button>
									</form>
								</div>
								<div class="col-lg-2">
									<!--  Edit Service Button -->
									<form action="editListing" method="GET">
										<input type="hidden" id="listingID" name="listingID"
											value="${service.getId()}" />
										<button class="btn btn-success btn-block">
											<span class="fa fa-info"></span> Edit Details
										</button>
									</form>
								</div>
							</div>
						</div>

					</c:forEach>
				</div>

				<!-- Users Accepted Requests -->
				<div class="request-row">
					<h3>Requests You've Offered To Help With</h3>

					<c:if test="${empty acceptedRequests}">
						<div class="card card-request">
							<h4 class="card-title">No Requests Accepted</h4>
							<p class="card-text">You can accept a request by visiting the
								Requests page from the navigation bar.</p>
						</div>
					</c:if>

					<c:forEach items="${acceptedRequests}" var="acceptedRequest">

						<div class="card card-request">
							<h4 class="card-title">${acceptedRequest.getTitle()}</h4>
							<h6 class="card-subtitle mb-2 text-muted">${acceptedRequest.getCity()},
								${acceptedRequest.getCounty()}</h6>
							<p class="card-text">${acceptedRequest.getFormattedDescription()}</p>

							<div class="row">
								<div class="col-lg-2">
									<c:choose>
										<c:when test="${acceptedRequest.getStatus().getOrdinal()==0}">
											<button class="btn btn-success btn-block" disabled>OPEN</button>
										</c:when>
										<c:when test="${acceptedRequest.getStatus().getOrdinal()==1}">
											<button class="btn btn-warning btn-block" disabled>PENDING</button>
										</c:when>
										<c:when test="${acceptedRequest.getStatus().getOrdinal()==2}">
											<button class="btn btn-danger btn-block" disabled>CLOSED</button>
										</c:when>
									</c:choose>
								</div>

								<div class="col-lg-6"></div>
								<div class="col-lg-2">
									<!-- Button to trigger modal -->
									<button class="btn btn-warning btn-block" data-toggle="modal"
										data-target="#modalForm_${acceptedRequest.userId}">
										<span class="fa fa-comments-o"></span> Message ${userIDtoUsernameMap.get(acceptedRequest.getUserId())}
									</button>
								</div>
								<div class="col-lg-2">
									<form action="viewListing" method="POST">
										<sec:csrfInput />
										<input type="hidden" id="listingType" name="listingType"
											value="${acceptedRequest.getListingType().getName()}" /> <input
											type="hidden" id="listingID" name="listingID"
											value="${acceptedRequest.getId()}" />
										<button class="btn btn-info btn-block">
											<span class="fa fa-info"></span> View Details
										</button>
									</form>
								</div>
							</div>
						</div>
						<!-- Modal -->
						<div class="modal fade" id="modalForm_${acceptedRequest.userId}"
							role="dialog">
							<div class="modal-dialog">
								<div class="modal-content">
									<!-- Modal Header -->
									<div class="modal-header">
										<h4 class="modal-title"
											id="myModalLabel_${acceptedRequest.userId}">Message ${userIDtoUsernameMap.get(acceptedRequest.getUserId())}</h4>
										<button type="button" class="close" data-dismiss="modal">
											<span aria-hidden="true">&times;</span> <span class="sr-only">Close</span>
										</button>
									</div>

									<!-- Modal Body -->
									<div class="modal-body">
										<p class="statusMsg"></p>
										<form role="form">
											<div class="form-group">
												<textarea class="form-control"
													id="inputMessage_${acceptedRequest.userId}"
													placeholder="Please enter your message to the user. Once you press send, you will see it appear on your My Messages page."></textarea>
											</div>

										</form>
									</div>

									<!-- Modal Footer -->
									<div class="modal-footer">
										<button type="button" class="btn btn-default"
											data-dismiss="modal">Cancel</button>
										<button type="button" class="btn btn-info submitBtn"
											onclick="submitContactForm(${acceptedRequest.userId})">SEND</button>
									</div>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
</body>
<script>
	//ajax function to send message. Targets the SendUserMessage class
	//userID may come in with the format "R_####", this is to differentiate the different created modals.
	//trim off the R_ for the user id that we are targeting but keep the original reference so we can reference the correct modals
	function submitContactForm(userID) {
		var targetID = userID;
		if(userID.includes("R_")){
			targetID = targetID.replace("R_","");
		}
		var message = $('#inputMessage_'+userID).val();
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		if (message.trim() == '') {
			alert('Please enter your message.');
			$('#inputMessage_'+userID).focus();
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
								$('#inputMessage_'+userID).val('');
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
							$('#modalForm_'+userID).modal('hide');
							$('body').removeClass('modal-open');
							$('.modal-backdrop').remove();
						}
					});
		}
	}
	
</script>
</html>
