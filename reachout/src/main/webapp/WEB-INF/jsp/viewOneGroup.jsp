<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>ReachOut | ${ListingObj.getListingType().getName()}</title>
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
						<strong>${ListingObj.getTitle()}</strong>
					</h2>
					<p class="card-text">${ListingObj.getDescription()}</p>
					<hr>
					<p class="card-text">${ListingObj.getCity()},
						${ListingObj.getCounty()}</p>
					<hr>
					<p class="card-text">Status: ${ListingObj.getStatus()}</p>
					<div class="row">
						<c:choose>
							<c:when  test="${!enableOfferButton || !enableMessageButton}"><div class="col-sm-3"></div></c:when>
						</c:choose>
						
						

						<c:choose>
							<c:when test="${isAdmin == true}">
								<div class="col-lg-4"></div>
								<div class="col-lg-4">
									<form action="deleteGroup" method="GET">
										<input type="hidden" id="groupID" name="groupID"
											value="${group.getId}" />
										<button class="btn btn-success btn-block">
											<span class="fa fa-info"></span> Delete Group
										</button>
									</form>
								</div>
							</c:when>
						</c:choose>

						<div class="col-sm-3"></div>
					</div>

				</div>
			</div>
		</div>
	</div>
</body>
</html>
