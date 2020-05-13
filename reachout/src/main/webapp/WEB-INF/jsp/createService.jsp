<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/components/stylesheets.jsp"%>
<title>ReachOut | Create Service</title>
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
							<h2><strong>Create Service</strong></h2>
		
							<!-- New Service Form -->
							<form action="createService" method="POST" id="createService">
								<sec:csrfInput />
								<fieldset>

									<!-- Service Title -->
									<div class="form-group">
										<label for="userBio">Title</label>
										<input id="serTitle" name="serTitle"
											placeholder="Please give a quick summary of your service." type="text"
											required="required" class="form-control" maxlength="128" minlength="10">
									</div>

									<!-- Service Description -->
									<div class="form-group remainingCounterText">
										<label for="serDesc">Description</label>
										<textarea id="serDesc" name="serDesc" cols="40" rows="5" id="serDesc"
											placeholder="Tell us a little about your service. Please note, everyone on the site will be able to view this information."
											class="form-control" maxlength="2000" minlength="50" required="required"></textarea>
									</div>

									<!-- Remaining Characters -->
									<div class="row remainingCounter">
										<div class="col-sm-3"></div>
										<div class="col-sm-6"><br></div>
										<div class="col-sm-3">
											<span id='remainingC'></span>
										</div>
									</div>

									<!-- Service County -->
									<div class="form-group">
										<label for="serCounty" >County of Service (e.g. Cambridgeshire)</label>
										<input id="serCounty" name="serCounty"
											placeholder="Countyshire" type="text"
											class="form-control" required="required" maxlength="26">
									</div>

									<!-- Service Town -->
									<div class="form-group">
										<label for="serCity">City/Town of Service (e.g. Chelsea)</label>
										<input id="serCity" name="serCity" required="required"
											placeholder="Town city" type="text"
											class="form-control" maxlength="60">
									</div>

									<!-- Create Button -->
									<button name="submit" type="submit" class="btn btn-primary btn-large btn-block">Create Service</button>

								</fieldset>
							</form>
						</div>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${createSuccess}">

								<!-- Display success message -->
								<div class="alert alert-success alert-spacing" role="alert">
									<p>Success! Your service is now live. Please visit your <a href="profile">Profile</a> to see your offered services.</p>
								</div>

							</c:when>
						</c:choose>
					</c:otherwise>
				</c:choose>
			</div>
		</div>	
		<div class="col-sm-4"></div>
	</div>

	<script>
		//Disable the submit button if there were no validation errors on the form
		$(document).ready(function () {
			var len = 0;
			var maxchar = 2000;

			$( '#serDesc' ).keyup(function(){
				len = this.value.length
				if(len > maxchar){
					return false;
				}
				else if (len > 0) {
					$( "#remainingC" ).html(maxchar - len);
				}
				else {
					$( "#remainingC" ).html(maxchar);
				}
			})

			$("#createService").submit(function () {
				$(this).find(':submit').attr('disabled', 'disabled');
			});
	
			$("#createService").bind("invalid-form.validate", function () {
				$(this).find(':submit').prop('disabled', false);
			});
		});
	</script>
</body>

</html>