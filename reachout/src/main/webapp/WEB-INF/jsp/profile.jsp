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
							<h4 class="card-title">${request.getTitle()}</h4>
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
										</c:when>
										<c:when test="${request.getStatus().getOrdinal()==2}">
											<button class="btn btn-danger btn-block" disabled>CLOSED</button>
										</c:when>
									</c:choose>
								</div>

								<div class="col-lg-6"></div>

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

								<div class="col-lg-8"></div>

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

					</c:forEach>
				</div>
			</div>
		</div>
	</div>
</body>

</html>
