<!DOCTYPE html>
<html lang="en-GB">
<head>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<title>Sign Up</title>
<%@ include file="/components/stylesheets.jsp"%>

</head>

<body>
	<div class="container-fluid">
		<%@ include file="/components/topHeader.jsp"%>
		<%@ include file="/components/navbar.jsp"%>
		<div class="row">
			<div class="col-sm-4"></div>
			<div class="col-sm-4">
				<c:choose>
					<c:when test="${empty postSent}">
						<div class="signup-form">
							<form action="" method="POST">
							<sec:csrfInput/>
								<fieldset>
									<div id="legend">
										<legend class="">Register</legend>
									</div>
									<h2>Sign Up</h2>
									<p>Please fill in this form to create an account!</p>
									<hr>
									<div class="control-group">
										<!-- Username -->
										<div class="input-group">
											<div class="input-group-prepend">
												<span class="input-group-text" id="basic-addon1"><i class="fa fa-user"></i></span>
											</div>
											<input type="text" class="form-control" name="username" id="username" placeholder="Username" required="required">
										</div>
									</div>
									<div class="control-group">
										<!-- E-mail -->
										<div class="input-group">
											<div class="input-group-prepend">
												<span class="input-group-text" id="basic-addon1"><i class="fa fa-paper-plane"></i></span>
											</div>
											<input type="email" class="form-control" name="email" id="email" placeholder="Email Address" required="required">
										</div>
									</div>
									<div class="control-group">
										<!-- Password-->
										<div class="input-group">
											<div class="input-group-prepend">
												<span class="input-group-text" id="basic-addon1"><i class="fa fa-lock"></i></span>
											</div>
											<input type="password" class="form-control" name="password" id="password" placeholder="Password" required="required">
										</div>
									</div>
									<div class="control-group">
										<!-- Terms and Conditions Checkbox -->
										<div class="input-group">
											<div class="input-group-prepend">
												<span class="input-group-text" id="basic-addon1">
													<i class="fa fa-lock"></i>
													<i class="fa fa-check"></i>
												</span>
											</div>
											<input type="password" class="form-control" name="password_confirm" id="password_confirm" placeholder="Confirm Password" required="required">
										</div>
									</div>
									<div class="control-group">
										<label class="checkbox-inline"><input type="checkbox" required="required"> I accept the <a href="#">Terms of Use</a> &amp; <a href="#">Privacy Policy</a></label>
									</div>
									<div class="control-group">
										<!-- Button -->
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
								<h3>A confirmation email has been sent to the address
									provided: ${emailAddress}</h3>
									</br>
									</br>
									<h4>In the meantime, why not check out your new <a href="profile">profile!</a></h4>
									<p>It could probably do with some details about you.</p>
							</c:when>
							<c:otherwise>
								<h3>Something was wrong with the data you provided!</h3>
								<h3>${validationErrors}</h3>
							</c:otherwise>

						</c:choose>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="col-sm-4"></div>
		</div>
	</div>

</body>
</html>