<!DOCTYPE html>
<html lang="en-GB">
<head>
<title>ReachOut | Requests</title>
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
						<h1>All Requests</h1>
					</div>
					<div class="col-lg-3">
						<form action="createRequest">
							<button class="btn btn-success text-right">
								<span class="fa fa-plus-circle"></span> Create New Request
							</button>
						</form>
					</div>
				</div>
				<c:forEach items="${liveRequests}" var="request">

					<div class="card card-request">
						<h4 class="card-title">${request.getTitle()}</h4>
						<h6 class="card-subtitle mb-2 text-muted">${request.getCity()}, ${request.getCounty()}</h6>
						<p class="card-text">${request.getDescription()}</p>
						
						<hr>

						<div class="row">
							<div class="col-lg-9">
								<p class="text-muted">Created by JoeBloggs 3d ago.</p>
							</div>

							<div class="col-lg-3">
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

						</div>
					</div>
						
				</c:forEach>
			</div>
			<div class="col-lg-2"></div>
		</div>
	</div>
</body>

</html>