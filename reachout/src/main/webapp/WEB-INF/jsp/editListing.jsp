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
		<div class="row profile-row">
			<div class="col-sm-4"></div>
			<div class="col-sm-4">

				<c:choose>
					<c:when test="${empty postSent}">
						<div class="card card-bkg">
							<h2>
								<strong>Edit ${listing.getListingType().getName()}</strong>
							</h2>

							<!-- New Edit Form -->
							<form action="" method="POST" id="editListing">
								<sec:csrfInput />

								<!-- Title -->
								<div class="form-group">
									<label for="Title">Title</label> <input id="Title" name="Title"
										placeholder="Title" type="text" required="required"
										class="form-control" value="${listing.title}" maxlength="128"
										minlength="10">
								</div>

								<!-- Description -->
								<div class="form-group remainingCounterText">
									<label for="Desc">Description</label>
									<textarea id="Desc" name="Desc" cols="40" rows="5"
										placeholder="Description" class="form-control"
										maxlength="2000" minlength="10" required="required">${listing.description}</textarea>
								</div>

								<!-- Remaining Characters -->
								<div class="row remainingCounter">
									<div class="col-sm-3"></div>
									<div class="col-sm-6">
										<br>
									</div>
									<div class="col-sm-3">
										<span id='remainingC'></span>
									</div>
								</div>

								<!-- Street -->
								<div class="form-group">
									<label for="City">Street of Listing (e.g. Downing
										Street)</label> <input id="Street" name="Street" placeholder="Street"
										required="required" type="text" class="form-control"
										maxlength="60">
								</div>

								<!-- Town -->
								<div class="form-group">
									<label for="City">City/Town of Request (e.g. Chelsea)</label> <input
										id="City" name="City" placeholder="City/Town"
										required="required" type="text" class="form-control"
										value="${listing.city}" maxlength="60">
								</div>

								<!-- County -->
								<div class="form-group">
									<label for="County">County of Request (e.g.
										Cambridgeshire))</label> <input id="County" name="County"
										placeholder="County" type="text" class="form-control"
										required="required" value="${listing.county}" maxlength="26">
								</div>


								<!-- Status -->
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

								<!-- ID -->
								<input type="hidden" id="listingID" name="listingID"
									value="${listing.getId()}" />

								<!-- Update/Delete buttons -->
								<div class="form-group">
									<button name="submit" type="submit" value="update"
										class="btn btn-success" style="float: left;">Update</button>
									<button name="submit" type="submit" value="delete"
										class="btn btn-danger" style="float: right;">Delete</button>
								</div>
							</form>
						</div>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${changeSuccess}">
								<div class="alert alert-success" role="alert">Your post
									has been ${changeType}</div>
							</c:when>
						</c:choose>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<c:choose>
			<c:when test="${not empty error}">
				<div class="alert alert-warning" role="alert">An Error
					occurred: ${error}</div>
			</c:when>
		</c:choose>
	</div>

	<script>
		//Disable the submit button if there were no validation errors on the form
		$(document).ready(function() {
			var len = 0;
			var maxchar = 2000;

			$('#Desc').keyup(function() {
				len = this.value.length
				if (len > maxchar) {
					return false;
				} else if (len > 0) {
					$("#remainingC").html(maxchar - len);
				} else {
					$("#remainingC").html(maxchar);
				}
			})

			$("#editListing").submit(function() {
				$(this).find(':submit').attr('hidden', 'hidden');
			});

			$("#editListing").bind("invalid-form.validate", function() {
				$(this).find(':submit').prop('disabled', false);
			});
		});
	</script>
</body>

</html>

