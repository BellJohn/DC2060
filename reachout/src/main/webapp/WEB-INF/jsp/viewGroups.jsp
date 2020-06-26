<!DOCTYPE html>
<html lang="en-GB">
<head>
<title>ReachOut | Groups</title>
<%@ include file="/components/stylesheets.jsp"%>
<meta charset="UTF-8">
</head>

<body>
	<div class="container-fluid">
		<%@ include file="/components/topHeader.jsp"%>
		<%@ include file="/components/navbar.jsp"%>

		<div class="row">
			<div class="col-lg-2"></div>
			<div class="col-lg-8">
				<div class="row listing-page">

					<!-- Page heading -->
					<div class="col-lg-9">
						<h1>Groups</h1>
					</div>
					<div class="col-lg-3">
						<!-- Create new group button -->
						<form action="createGroup">
							<button class="btn btn-success text-right">
								<span class="fa fa-plus-circle"></span> Create New Group
							</button>
						</form>
					</div>
					<!-- Page description-->
					<div class="col-lg-12">
						<p>Here you can view all user created groups. If you would like to join a group, please press request to join and your request will be sent to the groups admin for approval. For more information on Groups, please see the help page.</p>
					</div>
				</div>

				<!-- Your Groups section -->
				<h3>Your Groups</h3>
				<c:choose>
					<c:when test="${empty userGroups}">
						<div class="card card-request request-row">
							<h4 class="card-title">You aren't currently in any groups.</h4>
							<p class="card-text">Why not join one of the groups below?</p>
						</div>
					</c:when>
					<c:otherwise>
						<c:forEach items="${userGroups}" var="request">
							<div class="card card-request">
								<div class="row">
									<div class="col-md-9">
										<h4 class="card-title">${request.getName()}</h4>
										<p class="card-text">${request.getDescription()}</p>
										<h6 class="card-subtitle mb-2 text-muted">${request.city}, ${request.county}</h6>
										<hr>
										<div class="row">
											<div class="col-md-6">
												<form action="viewOneGroup" method="POST">
													<sec:csrfInput />
													<input type="hidden" id="groupID" name="groupID"
														value="${request.getId()}" /> <input type="hidden"
														id="username" name="username" value="${username}" />
													<button class="btn btn-info btn-block">
														<span class="fa fa-info"></span> View Group
													</button>
												</form>
											</div>
										</div>
									</div>

									<div class="col-md-3">
										<c:choose>
											<c:when test="${empty request.getPicture()}">
												<img src="images/groupImage.png" alt="GroupLogo"
													class="img-fluid img-thumbnail" style="max-height: 164px;">
											</c:when>
											<c:otherwise>
												<img src="${uploadDirectory}${request.getPicture()}?cache=0"
													alt="groupPic" class="img-fluid img-thumbnail" style="max-height: 164px;">
											</c:otherwise>
										</c:choose>
									</div>
								</div>
							</div>
						</c:forEach>
					</c:otherwise>
				</c:choose>

				<!-- User has no pending group requests -->
				<h3 class="pt-3">Your Pending Group Requests</h3>
				<c:choose>
					<c:when test="${empty pendingGroups}">
						<div class="card card-request request-row">
							<h4 class="card-title">No pending groups to display.</h4>
							<p class="card-text">Why not try joining a group below?</p>
						</div>
					</c:when>
				</c:choose>

				<!-- Users pending group requests -->
				<c:forEach items="${pendingGroups}" var="pendingGroup">
					<div class="card card-request">
						<div class="row">
							<div class="col-md-9">
								<h4 class="card-title">${pendingGroup.getName()}</h4>
								<p class="card-text">${pendingGroup.getDescription()}</p>
								<h6 class="card-subtitle mb-2 text-muted">${pendingGroup.city}, ${pendingGroup.county}</h6>
								<hr>
								<div class="row">
									<div class="col-md-6">
										<button class="btn btn-warning btn-block" disabled>Pending Approval</button>
									</div>
								</div>
							</div>
							<div class="col-md-3">
								<c:choose>
									<c:when test="${empty pendingGroup.getPicture()}">
										<img src="images/groupImage.png" alt="GroupLogo"
											class="img-fluid img-thumbnail" style="max-height: 164px;">
									</c:when>
									<c:otherwise>
										<img src="${uploadDirectory}${pendingGroup.getPicture()}?cache=0"
											alt="groupPic" class="img-fluid img-thumbnail" style="max-height: 164px;">
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
				</c:forEach>


				<!-- Groups that can be joined -->
				<h3 class="pt-3">Open Groups</h3>
				<c:choose>
					<c:when test="${empty otherGroups}">
						<div class="card card-request request-row">
							<h4 class="card-title">No new groups to display.</h4>
							<p class="card-text">If you're looking for one, why not create one above?</p>
						</div>
					</c:when>
				</c:choose>

				<c:forEach items="${otherGroups}" var="otherGroup">
					<div class="card card-request">
						<div class="row">
							<div class="col-md-9">
								<h4 class="card-title">${otherGroup.getName()}</h4>
								<p class="card-text">${otherGroup.getDescription()}</p>
								<h6 class="card-subtitle mb-2 text-muted">${otherGroup.city}, ${otherGroup.county}</h6>
								<hr>
								<div class="row">
									<div class="col-md-6">
										<form action="" method="POST">
											<sec:csrfInput />
											<input type="hidden" id="action" name="action" value="accept" />
											<input type="hidden" id="groupID" name="groupID"
												value="${otherGroup.getId()}" /> <input type="hidden"
												id="username" name="username" value="${username}" />
											<button class="btn btn-success btn-block">
												<span class="fa fa-handshake-o"></span> Request to Join
											</button>
										</form>
									</div>
								</div>
							</div>
							<div class="col-md-3">
								<c:choose>
									<c:when test="${empty otherGroup.getPicture()}">
										<img src="images/groupImage.png" alt="GroupLogo" class="img-fluid img-thumbnail" style="max-height: 164px;">
									</c:when>
									<c:otherwise>
										<img src="${uploadDirectory}${otherGroup.getPicture()}?cache=0"
										alt="groupPic" class="img-fluid img-thumbnail" style="max-height: 164px;">
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
				</c:forEach>

			</div>
			<div class="col-lg-2"></div>
		</div>
	</div>
</body>

</html>
