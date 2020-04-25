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
		<div class="jumbotron">
			<div class="row">All Active Services</div>
		</div>
		<div class="row" style="width: 70%; margin: 0 auto;">
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
</body>
</html>