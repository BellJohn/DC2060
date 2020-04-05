<!DOCTYPE html>
<html lang="en-GB">
<head>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<title>Login</title>
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
						to <br /> <br /> <c:out
							value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />.
					</font>
				</c:if>
				<form class="form form-horizontal"
					action="<c:url value='j_spring_security_check'/>" method="POST">
					<fieldset>
						<div id="legend">
							<legend class="">Login</legend>
						</div>
						<div class="control-group">
							<!-- Username -->
							<label class="control-label" for="username">Username</label>
							<div class="controls">
								<input type="text" id="username" name="username" placeholder=""
									class="input-xlarge">
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
							<!-- Button -->
							<div class="controls">
								<button class="btn btn-success">Login</button>
							</div>
						</div>
					</fieldset>
				</form>

			</div>
			<div class="col-sm-4"></div>
		</div>
	</div>

</body>
</html>