<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>ReachOut | Update ${listing.getListingType().getName()}</title>
<%@ include file="/components/stylesheets.jsp"%>
<meta charset="UTF-8">
</head>

<body>
	<div class="container-fluid">
		<%@ include file="/components/navbar.jsp"%>
		<div class="row">
			<div class="col-sm-4"></div>
			<c:choose>
				<c:when test="${empty postSent}">
					<div class="col-sm-4">
						<form action="" method="POST">
							<sec:csrfInput />
							<div class="form-group row">
								<label for="Title" class="col-4 col-form-label">Title</label>
								<div class="col-8">
									<input id="Title" name="Title" placeholder="Title" type="text"
										required="required" class="form-control"
										value="${listing.title}">
								</div>
							</div>
							<div class="form-group row">
								<label for="Desc" class="col-4 col-form-label">Description</label>
								<div class="col-8">
									<textarea id="Desc" name="Desc" cols="40" rows="5"
										placeholder="Description" class="form-control">${listing.description}</textarea>
								</div>
							</div>
							<div class="form-group row">
								<label for="County" class="col-4 col-form-label">Location
									(County)</label>
								<div class="col-8">
									<input id="County" name="County" placeholder="County"
										type="text" class="form-control" required="required"
										value="${listing.county}">
								</div>
							</div>
							<div class="form-group row">
								<label for="City" class="col-4 col-form-label">Location
									(City/Town)</label>
								<div class="col-8">
									<input id="City" name="City" placeholder="City/Town"
										type="text" class="form-control" value="${listing.city}">
								</div>
							</div>

							<div class="form-group">
								<label for="listingStatus">Status</label> <select
									id="listingStatus" name="listingStatus" class="form-control">
									<c:forEach var="possibleListingStatus"
										items="${listingStatusList}">
										<option
											${possibleListingStatus == listing.status.toString() ? 'selected' : ''}><c:set
												var="status" value="${possibleListingStatus}" />
											<c:out value="${status.toString()}" /></option>
									</c:forEach>
								</select>
							</div>
							<input type="hidden" id="listingID" name="listingID"
								value="${listing.getId()}" />
							<div class="form-group row">
								<div class="col-sm-12">
									<button name="submit" type="submit" value="update" class="btn btn-success" style="float:left;">Update</button>
									<button name="submit" type="submit" value="delete" class="btn btn-danger" style="float:right;">Delete</button>
								</div>
								<div class="offset-4 col-5">
									
								</div>
							</div>
						</form>
					</div>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${changeSuccess}">
							<div class="alert alert-success" role="alert">Your post has been ${changeType}</div>
						</c:when>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</div>
		<c:choose>
			<c:when test="${not empty error}">
				<div class="alert alert-warning" role="alert">An Error
					occurred: ${error}</div>
			</c:when>
		</c:choose>
	</div>
</body>

</html>

