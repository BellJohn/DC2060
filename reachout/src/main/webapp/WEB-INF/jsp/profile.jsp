<!DOCTYPE html>
<html lang="en-GB">
<head>
<title>ReachOut | Profile</title>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/components/stylesheets.jsp"%>
</head>

<body>
	<div class="container-fluid">
		<%@ include file="/components/navbar.jsp"%>

		<div class="row profile-row">

			<!-- User Profile Card -->
			<div class="col-md-3">
				<div class="card card-bkg">

				<!-- Display Users Name -->
				<h2><strong>${firstName} ${lastName}</strong></h2>

				<!-- Display Users Image -->
				<figure>
					<img src="images/no-profile-pic.png" alt="" class="rounded-circle">
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

				<hr/>

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
				<a class="btn btn-primary btn-large btn-block update-profile-btn" href="updateProfile">Update Profile</a>
				</div>
			</div>

			<!-- Users Help Requests -->
			<div class="col-md-9">
				<h3>Requests Made</h3>
				<table class="table">
					<thead>
						<tr>
							<th scope="col">Request</th>
							<th scope="col">Description</th>
							<th scope="col">County</th>
							<th scope="col">City</th>
							<th scope="col">View</th>
						</tr>
					</thead>
					<c:forEach items="${liveRequests}" var="request">
						<tr>
							<th scope="row">${request.getTitle()}</th>
							<td>${request.getDescription()}</td>
							<td>${request.getCounty()}</td>
							<td>${request.getCity()}</td>
							<td><form action="viewListing" method="POST">
									<sec:csrfInput />
									<input type="hidden" id="listingType" name="listingType"
										value="${request.getListingType().getName()}" /> <input
										type="hidden" id="listingID" name="listingID"
										value="${request.getId()}" />
									<button class="btn btn-success btn-block">
										<span class="fa fa-info"></span> View Details
									</button>
								</form></td>
						</tr>
					</c:forEach>
				</table>

			<!-- Users Offered Services -->
				<h3>Services Offered</h3>
				<table class="table">
					<thead>
						<tr>
							<th scope="col">Service</th>
							<th scope="col">Description</th>
							<th scope="col">County</th>
							<th scope="col">City</th>
							<th scope="col">View</th>
						</tr>
					</thead>
					<c:forEach items="${liveServices}" var="service">
						<tr>
							<th scope="row">${service.getTitle()}</th>
							<td>${service.getDescription()}</td>
							<td>${service.getCounty()}</td>
							<td>${service.getCity()}</td>
							<td><form action="viewListing" method="POST">
									<sec:csrfInput />
									<input type="hidden" id="listingType" name="listingType"
										value="${service.getListingType().getName()}" /> <input
										type="hidden" id="listingID" name="listingID"
										value="${service.getId()}" />
									<button class="btn btn-success btn-block">
										<span class="fa fa-info"></span> View Details
									</button>
								</form></td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
	</div>
</body>

</html>
