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
		<div class="jumbotron">
			<div class="row">All Active Requests</div>
		</div>
		<div class="row" style="width: 70%; margin: 0 auto;">
			<table class="table">
				<thead>
					<tr>
						<th scope="col">Request</th>
						<th scope="col">Description</th>
						<th scope="col">County</th>
						<th scope="col">City</th>
						<th scope="col">Priority</th>
						<th scope="col">View</th>
					</tr>
				</thead>
				<c:forEach items="${liveRequests}" var="request">
					<tr>
						<th scope="row">${request.getTitle()}</th>
						<td>${request.getDescription()}</td>
						<td>${request.getCounty()}</td>
						<td>${request.getCity()}</td>
						<td>${request.getPriority()} </td>
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
	</div>
</body>
</html>