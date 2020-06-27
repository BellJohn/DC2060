<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>ReachOut | ${group.getName()}</title>
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
					<h2>
						<strong>${group.getName()}</strong>
					</h2>
					<p class="card-text">${group.city}, ${group.county}</p>
					<div class="row">
						<div class="col-md-4"></div>
						<div class="col-md-4">
							<c:choose>
								<c:when test="${empty group.getPicture()}">
									<img src="images/groupImage.png" alt="GroupLogo"
										class="rounded-circle" style="max-width: 50%;">
								</c:when>
								<c:otherwise>
									<img src="${group.getPicture()}?cache=0" alt="groupPic"
										style="max-width: 20%;">
								</c:otherwise>
							</c:choose>
						</div>
					</div>
					<p class="card-text">${group.getDescription()}</p>
					<hr>

					<c:choose>
						<c:when test="${isAdmin == true}">
							<div class="row">
								<div class="col-lg-6">
									<form action="editGroup" method="GET">
										<input type="hidden" id="groupID" name="groupID"
											value="${group.getId()}" />
										<button class="btn btn-success btn-block">
											<span class="fa fa-info"></span> Edit/Delete Group
										</button>
									</form>
								</div>
								<div class="col-lg-6">
									<form action="pendingGroupRequests" method="GET">
										<input type="hidden" id="groupID" name="groupID"
											value="${group.getId()}" />
										<button class="btn btn-success btn-block">
											<span class="fa fa-info"></span> View Pending Requests
										</button>
									</form>
								</div>
							</div>
						</c:when>
					</c:choose>

					<div class="col-sm-3"></div>
				</div>

			</div>
			<div class="col-md-3"></div>
		</div>
		<div class="row">
			<div class="col-lg-2"></div>
			<div class="col-lg-8">

				<h3 class="pt-3">Group Requests</h3>
				<!-- display all requests in this group -->
				<c:if test="${empty liveRequests}">
					<div class="card card-request">
						<h4 class="card-title">There are no Requests in this group yet.</h4>
						<p class="card-text">Check back later when other members of the group have raised requests.</p>
					</div>
				</c:if>

				<c:forEach items="${liveRequests}" var="request">
					<div class="card card-request pt-3">
						<div class="row">
							<h4 class="card-title col-md-9">${request.getTitle()}</h4>
							<h4 class="card-title col-md-3" style="text-align: right;">${request.getPriority()}</h4>
						</div>
						<h6 class="card-subtitle mb-2 text-muted">${request.getCity()}, ${request.getCounty()}</h6>
						<p class="card-text">${request.getFormattedDescription()}</p>
						<hr>
						<div class="row">
							<div class="col-lg-9">
								<p class="text-muted">Created by ${request.getUsername()}
									on ${request.getCreatedDate()} at ${request.getCreatedTime()}.</p>
							</div>
							<div class="col-lg-3">
								<form action="viewListing" method="POST">
									<sec:csrfInput />
									<input type="hidden" id="listingType" name="listingType"
										value="${request.getListingType()}" /> <input type="hidden"
										id="listingID" name="listingID"
										value="${request.getListingID()}" />
									<button class="btn btn-info btn-block">
										<span class="fa fa-info"></span> View Details
									</button>
								</form>
							</div>
						</div>
					</div>
				</c:forEach>

				<h3 class="pt-3">Group Services</h3>
				<!-- display all requests in this group -->
				<c:if test="${empty liveServices}">
					<div class="card card-request pt-3">
						<h4 class="card-title">There are no Services in this group yet.</h4>
						<p class="card-text">Check back later when other members of the group have raised services.</p>
					</div>
				</c:if>

				<c:forEach items="${liveServices}" var="service">
					<div class="card card-request pt-3">
						<h4 class="card-title">${service.getTitle()}</h4>
						<h6 class="card-subtitle mb-2 text-muted">${service.getCity()},
							${service.getCounty()}</h6>
						<p class="card-text">${service.getFormattedDescription()}</p>
						<hr>

						<div class="row">
							<div class="col-lg-9">
								<p class="text-muted">Created by ${service.getUsername()}
									on ${service.getCreatedDate()} at ${service.getCreatedTime()}.</p>
							</div>

							<div class="col-lg-3">
								<form action="viewListing" method="POST">
									<sec:csrfInput />
									<input type="hidden" id="listingType" name="listingType"
										value="${service.getListingType()}" /> <input type="hidden"
										id="listingID" name="listingID"
										value="${service.getListingID()}" />
									<button class="btn btn-info btn-block">
										<span class="fa fa-info"></span> View Details
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
