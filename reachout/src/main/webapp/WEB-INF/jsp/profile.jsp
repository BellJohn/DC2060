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
		<div class="row">

			<!-- PROFILE INFO (ETC) -->
			<div class="col-md-offset-4 col-md-8 col-lg-offset-3 col-lg-6">
				<div class="well profile">
					<div class="col-sm-12">
						<div class="col-xs-12 col-sm-8" style="width: 981px; ">
						
							<h2>${firstName} ${lastName}</h2>
							<p>
								<strong>User Bio:
								 </strong> 
								 <c:choose>
  									  <c:when test="${empty bio}">
        							Tell us more about yourself
    								</c:when>
   									 <c:otherwise>
        							${bio}
    								</c:otherwise>
								</c:choose>
							</p>
						</div>
						<div class="col-xs-12 col-sm-6 text-center">
							<figure>
								<img src="images/no-profile-pic.png" alt=""
									class="img-circle img-responsive">
							</figure>
						</div>
						<div class="col-xs-12 col-sm-6 text-center">
						<c:choose>
  									  <c:when test="${empty healthStatus}">
        							Health Status : Unknown
    								</c:when>
   									 <c:otherwise>
   									 Health Status :  ${healthStatus}
    								</c:otherwise>
								</c:choose>
					</div>
					<div class="col-xs-12 col-sm-6 emphasis">
					<div class="btn-group dropup btn-block">
								<a class="btn btn-primary btn-large" href="updateProfile">Update Profile</a>
							</div>
					</div>
					<div class="col-xs-12 divider text-center">
						<div class="col-xs-12 col-sm-6 emphasis">
							<h2>
								<strong>1</strong>
							</h2>
							<p>
								<small>Requests</small>
							</p>
							<form action="createRequest">
								<button class="btn btn-success btn-block">
									<span class="fa fa-plus-circle"></span> Create New Request
								</button>
							</form>

						</div>
						<div class="col-xs-12 col-sm-6 emphasis">
							<h2>
								<strong>2</strong>
							</h2>
							<p>
								<small>Services Offered</small>
							</p>
							<form action="createService">
								<button class="btn btn-info btn-block">
									<span class="fa fa-user"></span> Offer New Service
								</button>
							</form>
						</div>
						<div class="col-xs-12 col-sm-6 emphasis">
							<div class="btn-group dropup btn-block">
								<button type="button" class="btn btn-primary">Options</button>
							</div>
						</div>
					</div>
				</div>
			</div>

			<!-- REQUESTED SERVICES -->
			<div class="col-lg-offset-9">
				<h1>Requests Made</h1>
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
			</div>


			<!-- OFFERED SERVICES -->
			<div class="col-lg-offset-9">
				<h1>Services Offered</h1>
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