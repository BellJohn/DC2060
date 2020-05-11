<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/components/stylesheets.jsp"%>
<title>ReachOut | Create Request</title>
</head>
<body>
	<div class="container-fluid">
		<%@ include file="/components/navbar.jsp"%>
		<div class="row profile-row">
			<div class="col-sm-4"></div>
			<div class="col-md-4">
				<c:choose>
					<c:when test="${empty postSent}">
						<div class="card card-bkg">
							<h2><strong>Create Request</strong></h2>
		
							<!-- New Request Form -->
							<form action="createRequest" method="POST">
								<sec:csrfInput />
								<fieldset>

									<!-- Request Title -->
									<div class="form-group">
										<label for="userBio">Title</label>
										<input id="reqTitle" name="reqTitle"
											placeholder="Please give a quick summary of your request." type="text"
											required="required" class="form-control" maxlength="128" minlength="10">
									</div>

									<!-- Request Description -->
									<div class="form-group">
										<label for="reqDesc">Description</label>
										<textarea id="reqDesc" name="reqDesc" cols="40" rows="5"
											placeholder="Tell us a little about your request. Please note, everyone on the site will be able to view this information."
											class="form-control" maxlength="2000" minlength="50" required="required"></textarea>
									</div>

									<!-- Request County -->
									<div class="form-group">
										<label for="reqCounty" >County of Request (e.g. Cambridgeshire)</label>
										<input id="reqCounty" name="reqCounty"
											placeholder="Countyshire" type="text"
											class="form-control" required="required" maxlength="26">
									</div>

									<!-- Request Town -->
									<div class="form-group">
										<label for="reqCity">City/Town of Request (e.g. Chelsea)</label>
										<input id="reqCity" name="reqCity" required="required"
											placeholder="Town city" type="text"
											class="form-control" maxlength="60">
									</div>

									<!-- Create Button -->
									<button name="submit" type="submit" class="btn btn-primary btn-large btn-block">Create Request</button>

								</fieldset>
							</form>
						</div>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${createSuccess}">

								<!-- Display success message -->
								<div class="alert alert-success alert-spacing" role="alert">
									<p>Success! Your request is now live. Please visit your <a href="profile">Profile</a> to see your open requests.</p>
								</div>

							</c:when>
						</c:choose>
					</c:otherwise>
				</c:choose>
			</div>
		</div>	
		<div class="col-sm-4"></div>
	</div>
</body>

</html>