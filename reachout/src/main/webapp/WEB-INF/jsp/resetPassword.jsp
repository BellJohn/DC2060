<!DOCTYPE html>
<html lang="en-GB">

<head>
	<title>ReachOut | Reset Password</title>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ include file="/components/stylesheets.jsp"%>
</head>

<body>
	<div class="container-fluid">
		<%@ include file="/components/topHeader.jsp"%>
		<%@ include file="/components/navbar.jsp"%>
		<div class="row">
			<div class="col-sm-3"></div>
			<div class="col-sm-6">
				<c:choose>

					<c:when test="${not empty codeValid}">
						<!-- When the user enters the page with a reset code-->
						<c:choose>
							<c:when test="${codeValid}">
								<!-- Enter new password form -->
								<div class="signup-login-form login-form">
									<form action="" method="POST" id="resetPassword">
										<sec:csrfInput />
										<fieldset>
											<!-- Header info -->
											<div id="legend">
												<legend class=""><h2>Reset Password</h2></legend>
											</div>
											<p>Please enter your new password below.</p>
											<hr>
											<!-- Password-->
											<div class="control-group">
												<div class="input-group">
													<div class="input-group-prepend">
														<span class="input-group-text" id="basic-addon1"><i
																class="fa fa-lock fa-fw"></i></span>
													</div>
													<input type="password" class="form-control" name="password" id="password" minLength="8"
														placeholder="Password" required="required" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}"
														onchange="this.setCustomValidity(this.validity.patternMismatch ? 'Please match the specified format.' : ''); if(this.checkValidity()) form.password_confirm.pattern = this.value;">
													<small id="passwordHelp" class="form-text text-muted">Your password must have at least 8 characters, a number, an uppercase letter and a lowercase letter.</small>
												</div>
											</div>
											<!-- Confirm Password -->
											<div class="control-group">	
												<div class="input-group">
													<div class="input-group-prepend">
														<span class="input-group-text" id="basic-addon1">
															<i class="fa fa-lock fa-fw"></i>
														</span>
													</div>
													<input type="password" class="form-control" name="password_confirm" minLength="8"
														id="password_confirm" placeholder="Confirm Password" required="required"
														onchange="this.setCustomValidity(this.validity.patternMismatch ? 'Passwords do not match.' : '');">
												</div>
											</div>
											<!-- Users ID -->
											<div class="control-group">	
												<div class="input-group">
													<input type="number" class="form-control" name="userId" id="userId" value="${userId}" hidden="true">
												</div>
											</div>
											<!-- Reset Button -->
											<div class="control-group">
												<button class="btn btn-primary btn-lg">Reset</button>
											</div>
										</fieldset>
									</form>
								</div>
							</c:when>

							<c:otherwise>
								<!-- Code Expired -->
								<br>
								<div class="alert alert-danger text-center" role="alert">
									<h5>The entered code is either invalid or has expired. Please request a new reset code via the login page.</h5>
								</div>
							</c:otherwise>

						</c:choose>
					</c:when>

					<c:when test="${submit && reset}">
						<!-- Email confirmation -->
						<br>
						<div class="alert alert-success text-center" role="alert">
							<h5>Your password has now been updated.</h5>
						</div>
					</c:when>

					<c:when test="${submit && !reset}">
						<!-- Email confirmation -->
						<br>
						<div class="alert alert-success text-center" role="alert">
							<h5>If the account exists, a password reset email has been sent.</h5>
						</div>
					</c:when>
					
					<c:otherwise>
						<!-- Form to send reset email -->
						<div class="signup-login-form login-form">
							<form action="" method="POST" id="resetPassword">
								<sec:csrfInput />
								<fieldset>
									<!-- Header info -->
									<div id="legend">
										<legend class=""><h2>Reset Password</h2></legend>
									</div>
									<p>Please enter the email associated with your account.</p>
									<hr>
									<!-- Email Address -->
									<div class="control-group">
										<div class="input-group">
											<div class="input-group-prepend">
												<span class="input-group-text" id="basic-addon1">
													<i class="fa fa-envelope-square fa-fw"></i></span>
											</div>
											<input type="text" class="form-control" name="email" id="email"
												placeholder="Email Address" required="required" maxlength="90"
												pattern="^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$"
												title="Please enter a valid email address">
										</div>
									</div>
									<!-- Reset Button -->
									<div class="control-group">
										<button class="btn btn-primary btn-lg">Reset</button>
									</div>
								</fieldset>
							</form>
						</div>
					</c:otherwise>

				</c:choose>
			</div>
			<div class="col-sm-3"></div>
		</div>
	</div>

	<script>
		//Disable the submit button if there were no validation errors on the form
		$(document).ready(function () {
			$("#resetPassword").submit(function () {
				$(this).find(':submit').attr('disabled', 'disabled');
			});
	
			$("#resetPassword").bind("invalid-form.validate", function () {
				$(this).find(':submit').prop('disabled', false);
			});
		});
	</script>
</body>

</html>