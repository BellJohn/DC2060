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
					<div class="col-lg-9">
						<h1>Groups</h1>
					</div>
					<div class="col-lg-3">
						<form action="createGroup">
							<button class="btn btn-success text-right">
								<span class="fa fa-plus-circle"></span> Create New Group
							</button>
						</form>
					</div>
					<div class="col-lg-12">
						<p>Here you can view all of the user created groups. If you
							would like to join a group, please press request to join and your
							request will be sent to admin for approval. For more information
							on Groups, please see the help page.</p>
					</div>
				</div>
				<h3>Your Groups</h3>

				<c:choose>
					<c:when test="${empty userGroups}">
							You are not in any groups, why not join one of the groups below?
						</c:when>
					<c:otherwise>

						<c:forEach items="${userGroups}" var="request">


							<div class="card card-request">
								<div class="row">
									<h4 class="card-title col-md-3">${request.getName()}</h4>
									<div class="col-md-6"></div>
									<!-- Display Group Image -->
										<c:choose>
											<c:when test="${empty request.getPicture()}">
												<img src="images/groupImage.png" alt="GroupLogo"
													class="rounded-circle" style="max-width: 20%;">
											</c:when>
											<c:otherwise>
												<img src="${groupPic}" alt="groupPic" class="rounded-circle" style="max-width: 20%;">
											</c:otherwise>
										</c:choose>
								</div>
								<div class="row">
									<h5 class="card-title col-md-12" style="text-align: center;">${request.getDescription()}</h5>
								</div>

								Location
								<h6 class="card-subtitle mb-2 text-muted">${request.getLocationId()}</h6>
								<hr>

								<div class="row">
									<div class="col-lg-3">
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

						</c:forEach>
					</c:otherwise>
				</c:choose>


				<h3>Other groups</h3>
				
					<c:forEach items="${pendingGroups}" var="pendingGroup">


					<div class="card card-request">
						<div class="row">
							<h4 class="card-title col-md-3">${pendingGroup.getName()}</h4>
							<div class="col-md-6"></div>
							<!-- Display Group Image -->
								<c:choose>
									<c:when test="${empty pendingGroup.getPicture()}">
										<img src="images/groupImage.png" alt="GroupLogo"
													class="rounded-circle" style="max-width: 20%;">
									</c:when>
									<c:otherwise>
										<img src="${pendingGroup.getPicture()}" alt="groupPic" style="max-width: 20%;">
									</c:otherwise>
								</c:choose>
						</div>
						
						<div class="row">
							<h5 class="card-title col-md-12" style="text-align: center;">${pendingGroup.getDescription()}</h5>
						</div>
						<div class="row"> <h4 class="card-title col-md-3" style="color:#D3D3D3;"> Pending approval</h4> </div>

						Location
						<h6 class="card-subtitle mb-2 text-muted">${pendingGroup.getLocationId()}</h6>
						<hr>
					</div>
				</c:forEach>


				<c:forEach items="${otherGroups}" var="otherGroup">


					<div class="card card-request">
						<div class="row">
							<h4 class="card-title col-md-3">${otherGroup.getName()}</h4>
							<div class="col-md-6"></div>
							<!-- Display Group Image -->
							
								<c:choose>
									<c:when test="${empty otherGroup.getPicture()}">
										<img src="images/groupImage.png" alt="GroupLogo"
													class="rounded-circle" style="max-width: 20%;">
									</c:when>
									<c:otherwise>
										<img src="${otherGroup.getPicture()}" alt="groupPic" style="max-width: 20%;">
									</c:otherwise>
								</c:choose>
							
						</div>
						<div class="row">
							<h5 class="card-title col-md-12" style="text-align: center;">${otherGroup.getDescription()}</h5>
						</div>
						Location
						<h6 class="card-subtitle mb-2 text-muted">${otherGroup.getLocationId()}</h6>
						<hr>
						<div class="row">
							<div class="col-lg-6">
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
				</c:forEach>

			</div>
			<div class="col-lg-2"></div>
		</div>
	</div>
</body>

</html>
