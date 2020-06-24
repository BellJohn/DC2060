<!DOCTYPE html>
<html lang="en-GB">

<head>
	<title>ReachOut | Sign Up</title>
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
					<c:when test="${empty postSent}">
						<div class="signup-login-form">
							<form action="" method="POST" id="signup">
								<sec:csrfInput />
								<fieldset>
									<div id="legend">
										<legend class="">
											<h2>Sign Up</h2>
										</legend>
									</div>
									<p>Please fill in the fields below to create an account.</p>
									<hr>
									<div class="row">
										<div class="col">
											<!-- First name -->
											<div class="control-group">
												<div class="input-group">
													<div class="input-group-prepend">
														<span class="input-group-text" id="basic-addon1">
															<i class="fa fa-user fa-fw"></i></span>
													</div>
													<input type="text" class="form-control" name="firstName" id="firstName"
														placeholder="First Name" required="required" maxlength="50">
												</div>
											</div>
										</div>
										<div class="col">
											<!-- Last Name -->
											<div class="control-group">
												<div class="input-group">
													<input type="text" class="form-control" name="lastName" id="lastName"
														placeholder="Last Name" required="required" maxlength="50">
												</div>
											</div>
										</div>
									</div>

									<!-- Username -->
									<div class="control-group">
										<div class="input-group">
											<div class="input-group-prepend">
												<span class="input-group-text" id="basic-addon1">
													<i class="fa fa-at fa-fw"></i></span>
											</div>
											<input type="text" class="form-control" name="username" id="username"
												placeholder="Username" required="required" maxlength="50">
										</div>
									</div>

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

									<!-- Date of Birth-->
									<div class="control-group">
										<div class="input-group">
											<div class="input-group-prepend">
												<span class="input-group-text" id="basic-addon1">
													<i class="fa fa-calendar fa-fw"></i></span>
											</div>
											<!-- check date of birth is a suitable date including leap years -->
											<input type="text" autocomplete="off" pattern="(^(((0[1-9]|1[0-9]|2[0-8])[\/](0[1-9]|1[012]))|((29|30|31)[\/](0[13578]|1[02]))|((29|30)[\/](0[4,6,9]|11)))[\/](19|[2-9][0-9])\d\d$)|(^29[\/]02[\/](19|[2-9][0-9])(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)$)" 
											class="form-control" name="dob" id="dob" title="Please use format DD/MM/YYYY"
												placeholder="Date of Birth (DD/MM/YYYY)" required="required" >
										</div>
									</div>

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
									
									<!-- Sign Up Button -->
									<div class="control-group">
										<button class="btn btn-primary btn-lg">Sign Up</button>
									</div>

								</fieldset>
							</form>
							<div class="text-center">Already have an account? <a href="login">Login here</a></div>
						</div>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${postResult}">
								<br>
								<div class="alert alert-success" role="alert">
									<h3>A confirmation email has been sent to the address
										provided: ${emailAddress}</h3>
								</div>
								</br>
								<h4>In the meantime, why not check out your new <a href="profile">profile!</a></h4>
								<p>It could probably do with some details about you.</p>
							</c:when>
							<c:otherwise>
								<br>
								<div class="alert alert-danger" role="alert">
									<p>Something was wrong with the data you provided!</p>
									<br>
									<p>${validationErrors}</p>
								</div>
							</c:otherwise>
						</c:choose>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="col-sm-3"></div>
		</div>
	</div>

	<script>
		//Disable the submit button if there were no validation errors on the form
		$(document).ready(function () {
			$("#signup").submit(function () {
				$(this).find(':submit').attr('disabled', 'disabled');
			});
	
			$("#signup").bind("invalid-form.validate", function () {
				$(this).find(':submit').prop('disabled', false);
			});
		});
	</script>
</body>

</html>