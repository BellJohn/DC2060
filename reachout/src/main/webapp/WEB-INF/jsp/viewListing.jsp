<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>ReachOut - ${ListingObj.getListingType().getName()}</title>
<%@ include file="/components/stylesheets.jsp"%>
<meta charset="UTF-8">
</head>

<body>
	<div class="container-fluid">
		<%@ include file="/components/topHeader.jsp"%>
		<%@ include file="/components/navbar.jsp"%>
		<div class="card">
			<div class="card-body card-header"
				style="width: 40%; margin: 0 auto;">
				<h4 class="card-title">${ListingObj.getTitle()}</h4>
				<p class="card-text">Description: ${ListingObj.getDescription()}</p>
				<ul class="list-group list-group-flush">
					<li class="list-group-item">County: ${ListingObj.getCounty()}</li>
					<li class="list-group-item">City: ${ListingObj.getCity()}</li>
					<li class="list-group-item">Status: ${ListingObj.getStatus()}</li>
				</ul>
			</div>
		</div>
	</div>
</body>
</html>

