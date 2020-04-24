<!DOCTYPE html>
<html lang="en-GB">

<head>
	<title>ReachOut | Login</title>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ include file="/components/stylesheets.jsp"%>
</head>

<body>
	<div class="container-fluid">
		<%@ include file="/components/topHeader.jsp"%>
		<%@ include file="/components/navbar.jsp"%>
		<div class="row">
			<div class="col-sm-4"></div>
			<div class="col-sm-4">

				<c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
					<font color="red"> Your login attempt was not successful due
						to <br /> <br />
						<c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />.
					</font>
				</c:if>
				<div class="signup-form">
					<form action="<c:url value='j_spring_security_check'/>" method="POST">
						<sec:csrfInput />
						<fieldset>
							<div id="legend">
								<legend class=""><h2>Login</h2></legend>
							</div>
							<p>Please enter your login details below.</p>
							<hr>
							<div class="control-group">
								<!-- Username -->
								<div class="input-group">
									<div class="input-group-prepend">
										<span class="input-group-text" id="basic-addon1">
											<i class="fa fa-user"></i>
										</span>
									</div>
									<input type="text" class="form-control" name="username" id="username"
										placeholder="Username" required="required">
								</div>
							</div>
							<div class="control-group">
								<!-- Password-->
								<div class="input-group">
									<div class="input-group-prepend">
										<span class="input-group-text" id="basic-addon1">
											<i class="fa fa-lock"></i>
										</span>
									</div>
									<input type="password" class="form-control" name="password" id="password"
										placeholder="Password" required="required">
								</div>
							</div>
							<div class="control-group">
								<!-- Button -->
								<button class="btn btn-primary btn-lg">Login</button>
							</div>
						</fieldset>
					</form>
					<div class="text-center">Don't have an account? <a href="signup">Sign Up Here</a></div>
				</div>

			</div>
			<div class="col-sm-4"></div>
		</div>
	</div>

</body>

</html>