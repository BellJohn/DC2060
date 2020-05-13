<!DOCTYPE html>
<html lang="en-GB">
<head>
<title>ReachOut | Services</title>
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
						<h1>All Services</h1>
					</div>
					<div class="col-lg-3">
						<form action="createService">
							<button class="btn btn-info text-right ">
								<span class="fa fa-plus-circle"></span> Offer New Service
							</button>
						</form>
					</div>
				</div>
				<c:forEach items="${liveServices}" var="service">

					<div class="card card-request">
						<h4 class="card-title">${service.getTitle()}</h4>
						<h6 class="card-subtitle mb-2 text-muted">${service.getCity()}, ${service.getCounty()}</h6>
						<p class="card-text">${service.getFormattedDescription()}</p>
						
						<hr>

						<div class="row">
							<div class="col-lg-9">
								<p class="text-muted">Created by ${service.getUsername()} on ${service.getCreatedDate()} at ${service.getCreatedTime()}.</p>
							</div>

							<div class="col-lg-3">
								<form action="viewListing" method="POST">
									<sec:csrfInput />
									<input type="hidden" id="listingType" name="listingType"
										value="${service.getListingType()}" /> <input
										type="hidden" id="listingID" name="listingID"
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