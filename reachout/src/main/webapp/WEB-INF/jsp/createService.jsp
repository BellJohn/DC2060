<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/components/stylesheets.jsp"%>
<title>New Service</title>
</head>
<body>
	<div class="container-fluid">
		<%@ include file="/components/navbar.jsp"%>
		<div class="row">
			<div class="col-sm-4"></div>
			<c:choose>
				<c:when test="${empty postSent}">
					<div class="col-sm-4">
						<form action="createService" method="POST">
							<sec:csrfInput />
							<div class="form-group row">
								<label for="serTitle" class="col-4 col-form-label">Service
									Title</label>
								<div class="col-8">
									<input id="serTitle" name="serTitle"
										placeholder="Quick summary of what help you can provide..." type="text"
										required="required" class="form-control">
								</div>
							</div>
							<div class="form-group row">
								<label for="serDesc" class="col-4 col-form-label">Description</label>
								<div class="col-8">
									<textarea id="serDesc" name="serDesc" cols="40" rows="5"
										placeholder="Tell us a little about your service offer. What you're offering."
										class="form-control"></textarea>
								</div>
							</div>
							<div class="form-group row">
								<label for="serCounty" class="col-4 col-form-label">Location
									(County)</label>
								<div class="col-8">
									<input id="serCounty" name="serCounty"
										placeholder="What county is this service available for?" type="text"
										class="form-control" required="required">
								</div>
							</div>
							<div class="form-group row">
								<label for="serCity" class="col-4 col-form-label">Location
									(City/Town)</label>
								<div class="col-8">
									<input id="serCity" name="serCity"
										placeholder="What City/Town is this service located in?" type="text"
										class="form-control">
								</div>
							</div>
							<div class="form-group row">
								<div class="offset-4 col-8">
									<button name="submit" type="submit" class="btn btn-primary">Create
										Service Offer</button>
								</div>
							</div>
						</form>
					</div>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${createSuccess}">
							<!-- Display success message -->
							<div class="alert alert-success col-sm-4 alert-spacing" role="alert">
								<p>Success! Your service offer is now live. Please visit your <a href="/profile">Profile</a> to see your open requests.</p>
							</div>
						</c:when>
					</c:choose>
				</c:otherwise>
			</c:choose>
			<div class="col-sm-4"></div>
		</div>
	</div>
</body>
</html>