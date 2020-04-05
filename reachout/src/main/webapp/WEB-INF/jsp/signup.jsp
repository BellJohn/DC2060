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
						<form class="form form-horizontal" action='' method="POST">
						<sec:csrfInput/>
							<fieldset>
								<div id="legend">
									<legend class="">Register</legend>
								</div>
								<div class="control-group">
									<!-- Username -->
									<label class="control-label" for="username">Username</label>
									<div class="controls">
										<input type="text" id="username" name="username"
											placeholder="" class="input-xlarge">
										<p class="help-block">Username can contain any letters or
											numbers, without spaces</p>
									</div>
								</div>

								<div class="control-group">
									<!-- E-mail -->
									<label class="control-label" for="email">E-mail</label>
									<div class="controls">
										<input type="text" id="email" name="email" placeholder=""
											class="input-xlarge">
										<p class="help-block">Please provide your E-mail</p>
									</div>
								</div>

								<div class="control-group">
									<!-- Password-->
									<label class="control-label" for="password">Password</label>
									<div class="controls">
										<input type="password" id="password" name="password"
											placeholder="" class="input-xlarge">
										<p class="help-block">Please provide a password</p>
									</div>
								</div>

								<div class="control-group">
									<!-- Password -->
									<label class="control-label" for="password_confirm">Password
										(Confirm)</label>
									<div class="controls">
										<input type="password" id="password_confirm"
											name="password_confirm" placeholder="" class="input-xlarge">
										<p class="help-block">Please confirm your password</p>
									</div>
								</div>

								<div class="control-group">
									<!-- Button -->
									<div class="controls">
										<button class="btn btn-success">Register</button>
									</div>
								</div>
							</fieldset>
						</form>
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